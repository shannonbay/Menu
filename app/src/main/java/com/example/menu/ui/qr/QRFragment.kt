package com.example.menu.ui.qr

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.menu.MainActivity
import com.example.menu.QRCodeFoundListener
import com.example.menu.QRCodeImageAnalyzer
import com.example.menu.R
import com.example.menu.data.SecurePreferences
import com.example.menu.databinding.FragmentQrBinding
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.camera_preview.*
import kotlinx.android.synthetic.main.camera_preview.view.*
import java.lang.IllegalArgumentException
import java.util.concurrent.ExecutionException

class QRFragment: Fragment(), ActivityCompat.OnRequestPermissionsResultCallback, SecurePreferences {

    private var _binding: FragmentQrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        val root: View = binding.root

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        requestCamera();

        return root;
    }
    private var qrCode: String? = null
    private val PERMISSION_REQUEST_CAMERA = 0

    private fun requestCamera() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA
                )
            }
        }
    }
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private fun bindCameraPreview(cameraProvider: ProcessCameraProvider) {
        activity_main_previewView.preferredImplementationMode = PreviewView.ImplementationMode.SURFACE_VIEW
        val preview = Preview.Builder()
            .build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(activity_main_previewView.createSurfaceProvider())
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(requireContext()),
            QRCodeImageAnalyzer(object : QRCodeFoundListener {
                override fun onQRCodeFound(_qrCode: String?) {
                    qrCode = _qrCode
                    if(System.currentTimeMillis() - 2000 < scanTime) return;
                    scanTime = System.currentTimeMillis()
                    if ("Southern Cross Wellington Room 108".equals(_qrCode)) {
                        Toast.makeText(requireContext(), qrCode, Toast.LENGTH_SHORT).show()
                        Log.i(MainActivity::class.java.simpleName, "QR Code Found: $qrCode")
                        securePreferences()
                            .edit()
                            .putString("room_id", qrCode)
                            .apply()
                        try {
                            //TODO provide proper API instead of using Exceptions as Control-Flow.
                            findNavController().navigate(R.id.nav_home)
                        } catch (e: IllegalArgumentException) {

                        }
                    } else {
                        Toast.makeText(requireContext(), qrCode, Toast.LENGTH_SHORT).show()
                        Log.i(MainActivity::class.java.simpleName, "Unknown QR Code: $qrCode")
                    }
                }

                override fun qrCodeNotFound() {
                }
            })
        )
        val camera = cameraProvider.bindToLifecycle(
            (this as LifecycleOwner),
            cameraSelector,
            imageAnalysis,
            preview
        )
    }

    var scanTime: Long = 0L

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        cameraProviderFuture!!.addListener({
            try {
                val cameraProvider = cameraProviderFuture!!.get()
                bindCameraPreview(cameraProvider)
            } catch (e: ExecutionException) {
                Toast.makeText(requireContext(), "Error starting camera " + e.message, Toast.LENGTH_SHORT)
                    .show()
            } catch (e: InterruptedException) {
                Toast.makeText(requireContext(), "Error starting camera " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override var sharedPreferences: SharedPreferences? = null
}