package app.instamovies.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import app.instamovies.R
import app.instamovies.presentation.main.components.InstaMoviesApp
import app.instamovies.presentation.theme.InstaMoviesTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var updateManager: AppUpdateManager

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            InstaMoviesTheme {
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                val lifecycle = LocalLifecycleOwner.current.lifecycle
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()
                val updateLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                        if (result.resultCode != RESULT_OK) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(getString(R.string.update_failed))
                            }
                        }
                    }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                ) {
                    InstaMoviesApp(
                        windowSize = windowSize,
                        displayFeatures = displayFeatures,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                LaunchedEffect(viewModel, lifecycle) {
                    snapshotFlow { viewModel.uiState }
                        .filter { it.updateAvailable }
                        .flowWithLifecycle(lifecycle)
                        .collect {
                            it.updateInfo?.let { updateInfo ->
                                updateManager.startUpdateFlowForResult(
                                    updateInfo,
                                    updateLauncher,
                                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                                )
                            }
                        }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onEvent(MainUiEvent.OnResume)
    }
}
