package com.adempolat.otovinnapp.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.adempolat.otovinnapp.R
import com.adempolat.otovinnapp.data.response.DiscoverResponse
import com.adempolat.otovinnapp.databinding.FragmentHomeBinding
import com.adempolat.otovinnapp.databinding.ItemStationCategoryBinding
import com.adempolat.otovinnapp.usecases.StartVoiceRecognitionUseCase
import com.adempolat.otovinnapp.utils.Constants.REQUEST_RECORD_AUDIO_PERMISSION
import com.adempolat.otovinnapp.utils.showCustomToast
import com.adempolat.otovinnapp.view.adapter.MenuAdapter
import com.adempolat.otovinnapp.view.adapter.SliderAdapter
import com.adempolat.otovinnapp.view.adapter.StationItemAdapter
import com.adempolat.otovinnapp.view.adapter.StoryAdapter
import com.adempolat.otovinnapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var startVoiceRecognitionUseCase: StartVoiceRecognitionUseCase

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    internal lateinit var speechRecognizer: SpeechRecognizer

    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = object : Runnable {
        override fun run() {
            binding.viewPagerSlider?.let { viewPager ->
                val nextItem = (viewPager.currentItem + 1) % viewPager.adapter!!.itemCount
                viewPager.currentItem = nextItem
                sliderHandler.postDelayed(this, 5000) // 5 saniyede bir geçiş
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewStories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewMenu.layoutManager = GridLayoutManager(context, 4)

        val token = getToken()
        if (token.isNullOrEmpty()) {
            context?.showCustomToast(getString(R.string.token_invalid_or_missing), requireContext().getColor(R.color.colorWarn), R.drawable.logo)
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        } else {
            viewModel.fetchDiscoverData(token)
        }

        lifecycleScope.launch {
            viewModel.discoverData.collect { result ->
                result?.let {
                    if (it.code == 100) {
                        handleDiscoverResponse(it)
                    } else {
                        context?.showCustomToast(getString(R.string.error_occurred2, it.message), requireContext().getColor(R.color.colorError), R.drawable.logo)
                        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                    }
                }
            }
        }

        binding.ivVoiceSearch.setOnClickListener {
            checkAudioPermission()
        }

        setupSpeechRecognizer()
    }

    internal fun getToken(): String? {
        val sharedPref = activity?.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("TOKEN_KEY", null)
    }

    internal fun handleDiscoverResponse(response: DiscoverResponse) {
        val stories = response.data.stories
        val storyAdapter = StoryAdapter(stories, this)
        binding.recyclerViewStories.adapter = storyAdapter

        val menuItems = response.data.menu
        val menuAdapter = MenuAdapter(menuItems)
        binding.recyclerViewMenu.adapter = menuAdapter

        val sliderItems = response.data.slider
        val sliderAdapter = SliderAdapter(sliderItems.filter { it.url.isNotEmpty() })
        binding.viewPagerSlider.adapter = sliderAdapter
        sliderHandler.postDelayed(sliderRunnable, 5000) // 5 saniyede bir geçiş başlatma

        val stations = response.data.stations.sortedBy { it.order }
        val staticTitles = listOf(
            getString(R.string.nearby),
            getString(R.string.recent_services),
            getString(R.string.best_of_week)
        )  // servisten null geldiği için static olarak ayarlandı.

        stations.forEachIndexed { index, stationCategory ->
            val stationCategoryBinding = ItemStationCategoryBinding.inflate(layoutInflater)
            stationCategoryBinding.textViewStationTitle.text = staticTitles.getOrNull(index) ?: "Kategori"
            stationCategoryBinding.recyclerViewStationItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            stationCategoryBinding.recyclerViewStationItems.adapter = StationItemAdapter(stationCategory.station)
            binding.linearLayoutStations.addView(stationCategoryBinding.root)
        }
    }

    private fun getErrorText(errorCode: Int): String {
        return when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> getString(R.string.error_audio)
            SpeechRecognizer.ERROR_CLIENT -> getString(R.string.error_client)
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> getString(R.string.error_permissions)
            SpeechRecognizer.ERROR_NETWORK -> getString(R.string.error_network)
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> getString(R.string.error_network_timeout)
            SpeechRecognizer.ERROR_NO_MATCH -> getString(R.string.error_no_match)
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> getString(R.string.error_busy)
            SpeechRecognizer.ERROR_SERVER -> getString(R.string.error_server)
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> getString(R.string.error_timeout)
            else -> getString(R.string.error_unknown)
        }
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                binding.ivVoiceSearch.setImageResource(R.drawable.settings_voice_24px) // Dinleme başladığında mikrofonu değiştirme
                binding.ivVoiceSearch.background = context!!.getDrawable(R.drawable.mic_backgound_green)
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                binding.ivVoiceSearch.setImageResource(R.drawable.mic_24px) // Dinleme bittiğinde mikrofonu değiştirme
                binding.ivVoiceSearch.background = context!!.getDrawable(R.drawable.mic_background)
            }

            override fun onError(error: Int) {
                binding.ivVoiceSearch.setImageResource(R.drawable.mic_24px) // Hata oluştuğunda mikrofonu değiştirme
                val errorMessage = getErrorText(error)
                context?.showCustomToast(errorMessage, requireContext().getColor(R.color.colorError), R.drawable.logo)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    binding.etSearch.setText(matches[0])
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    internal fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
        } else {
            startVoiceRecognition()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startVoiceRecognition()
            } else {
                context?.showCustomToast(getString(R.string.error_permissions), requireContext().getColor(R.color.colorError), R.drawable.logo)
            }
        }
    }

    internal fun startVoiceRecognition() {
        val intent = startVoiceRecognitionUseCase.invoke("tr-TR", getString(R.string.speech_prompt))
        speechRecognizer.startListening(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognizer.destroy()
        sliderHandler.removeCallbacks(sliderRunnable)
        _binding = null
    }
}
