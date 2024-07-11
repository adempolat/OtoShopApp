package com.adempolat.otovinnapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.content.ContextCompat
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adempolat.otovinnapp.R
import com.adempolat.otovinnapp.data.response.DiscoverResponse
import com.adempolat.otovinnapp.usecases.StartVoiceRecognitionUseCase
import com.adempolat.otovinnapp.viewmodel.HomeViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var navController: NavController

    private lateinit var startVoiceRecognitionUseCase: StartVoiceRecognitionUseCase
    private lateinit var homeViewModel: HomeViewModel

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        startVoiceRecognitionUseCase = mockk()
        homeViewModel = mockk(relaxed = true)
    }

    @Test
    fun testFragmentNavigation() {
        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Base_Theme_OtoVinnApp).onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
            val token = "fake_token"
            every { fragment.getToken() } returns token
            fragment.onViewCreated(mockk(), mockk())

            verify { navController.navigate(R.id.action_homeFragment_to_loginFragment) }
        }
    }

    @Test
    fun testCheckAudioPermission() {
        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Base_Theme_OtoVinnApp).onFragment { fragment ->
            val mockContext = mockk<Context> {
                every { checkSelfPermission(Manifest.permission.RECORD_AUDIO) } returns PackageManager.PERMISSION_GRANTED
            }
            mockkStatic(ContextCompat::class)
            every { ContextCompat.checkSelfPermission(any(), Manifest.permission.RECORD_AUDIO) } returns PackageManager.PERMISSION_GRANTED

            fragment.checkAudioPermission()

            verify { fragment.startVoiceRecognition() }
        }
    }

    @Test
    fun testStartVoiceRecognition() {
        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Base_Theme_OtoVinnApp).onFragment { fragment ->
            val mockIntent = mockk<Intent>()
            every { startVoiceRecognitionUseCase.invoke(any(), any()) } returns mockIntent

            fragment.startVoiceRecognition()

            verify { fragment.speechRecognizer.startListening(mockIntent) }
        }
    }

    @Test
    fun testHandleDiscoverResponse() = runBlockingTest {
        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Base_Theme_OtoVinnApp).onFragment { fragment ->
            val response = mockk<DiscoverResponse>()
            every { response.code } returns 100
            every { homeViewModel.discoverData } returns MutableStateFlow(response)

            fragment.handleDiscoverResponse(response)

            // Add assertions for UI updates here
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
