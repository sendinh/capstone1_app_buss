package com.team03.dtuevent.ui.fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.team03.dtuevent.R;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.team03.dtuevent.callbacks.Consumer;
import com.team03.dtuevent.databinding.FragmentScanBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.team03.dtuevent.callbacks.CameraFailureCallback;
import com.team03.dtuevent.callbacks.CameraShutdownCallback;
import com.team03.dtuevent.callbacks.SetTouchListenerCallback;
import com.team03.dtuevent.callbacks.UseCaseCreator;
import com.team03.dtuevent.exceptions.NoCameraException;
import com.team03.dtuevent.exceptions.ReferenceInvalidException;
import com.team03.dtuevent.helper.Constant;
import com.team03.dtuevent.helper.HttpUtils;
import com.team03.dtuevent.helper.SharedPreferenceHelper;
import com.team03.dtuevent.objects.Availability;
import com.team03.dtuevent.objects.CamAccess;
import com.team03.dtuevent.objects.ScanningWrapper;
import com.team03.dtuevent.ui.activity.MainLoginActivity;
import com.team03.dtuevent.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ScanFragment extends Fragment {
    private final String cameraPermission = Manifest.permission.CAMERA;
    private CamAccess camAccessObj;
    private CameraShutdownCallback cameraShutdownCallback;
    private FragmentScanBinding binding;
    private ExecutorService camExecutor;
    private final static String TAG = "ScannerFragment";
    private ScanViewModel vm;
    BarcodeScannerOptions options =
            new BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                            Barcode.FORMAT_ALL_FORMATS)
                    .build();
    private final BarcodeScanner barcodeScanner = BarcodeScanning.getClient(options);

    private static final int CAMERA_REQUEST_CODE = 1000;
    private static final int GALLERY_REQUEST_CODE = 1001;
    private Camera camera;


    private String accountId;
    private String token;

    private CustomDialog dialog;

    private void readerBarcodeData(List<Barcode> barcodes) {
        if (barcodes.isEmpty()) {
            return;
        }

        String rawValue = barcodes.get(0).getRawValue().trim();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", rawValue);
            jsonObject.put("accountId", accountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(HttpUtils.getAbsoluteUrl("/api/event/addEventUser"))
                .addJSONObjectBody(jsonObject)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", String.format("Bearer %s", token))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ducnvx", "onResponse");
                        try {
                            String studentName = response.getString("name");
                            dialog.showConfirm(String.format("Hello %s", studentName), true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.showConfirm(String.format("Hello %s", rawValue), true);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ducnvx", "onError" + anError.getErrorCode());
                        dialog.showConfirm(String.format("Attendance Failed %s", rawValue), false);
                    }
                });


    }


    public void readLoad() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                mCameraProvider = cameraProvider;

                bindPreview(cameraProvider);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }

    private void scanQrcode(ImageProxy imageProxy) {
        @SuppressLint("UnsafeOptInUsageError") Image image = imageProxy.getImage();
        assert image != null;
        InputImage inputImage = InputImage.fromMediaImage(image, imageProxy.getImageInfo().getRotationDegrees());
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);
        scanner.process(inputImage)
                .addOnSuccessListener(barcodes -> readerBarcodeData(barcodes))
                .addOnFailureListener(e -> {
                    imageProxy.close();
                })
                .addOnCompleteListener(task -> {
                    if (task.getResult().isEmpty()) {
                        imageProxy.close();
                    }
                });
    }

    class ImageAnalyzer implements ImageAnalysis.Analyzer {

        @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
            scanQrcode(imageProxy);
        }
    }

    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private ListenableFuture cameraProviderFuture;
    private ImageAnalyzer imageAnalyzer;
    private ProcessCameraProvider mCameraProvider;


    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        ImageCapture imageCapture = new ImageCapture.Builder().build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(cameraExecutor, imageAnalyzer);
        cameraProvider.unbindAll();
        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentScanBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true);


        cameraExecutor = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance((getActivity()));
        imageAnalyzer = new ImageAnalyzer();

        accountId = SharedPreferenceHelper.getSharedPreferenceString(getContext(), Constant.ACCOUNT_ID_KEY, "");
        token = SharedPreferenceHelper.getSharedPreferenceString(getContext(), Constant.TOKEN_KEY, "");

        dialog = new CustomDialog(getActivity(), new CustomDialog.OnInputDialogClicked() {
            @Override
            public void onOkiButton() {
                readLoad();
            }
        });


        // Camera Use Cases:
//        UseCaseCreator useCaseCreator = () -> {

        // Use Case 1: Preview
        Preview preview = new Preview.Builder().build();
        if (binding != null && binding.previewView != null) {
            previewView = binding.previewView;
//                preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());
        }


        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                mCameraProvider = cameraProvider;

                bindPreview(cameraProvider);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getActivity()));


        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_scan, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        if (camAccessObj.getFlash() == Availability.UNAVAILABLE) {
//            menu.findItem(R.id.flash_toggle).setEnabled(false);
//        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.flash_toggle) {
            flashToggle();
        } else if (itemId == R.id.open_from_gallery) {
            analyzeFromGallery();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Handle intent.
        Intent intent = requireActivity().getIntent();
        if (!(intent == null || intent.getType() == null || !intent.getType().startsWith("image/"))) {
            // Get data.
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            processImageUri(uri);
            requireActivity().setIntent(null);
        }

        // Initialise ViewModel
        vm = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ScanViewModel.class);

        camExecutor = Executors.newSingleThreadExecutor();

        vm.getCodes().observe(getViewLifecycleOwner(), scanningWrappers -> {
            for (ScanningWrapper s : scanningWrappers) {
                s.display(getContext(), !vm.getBatchScanEnabled());
            }
        });


        vm.getModelDownloaded().observe(getViewLifecycleOwner(), downloaded -> {
            if (downloaded == Boolean.FALSE) {
                Snackbar.make(view, R.string.no_model, Snackbar.LENGTH_LONG).show();
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        if (prefs.contains("batch_scan")) {
            vm.setBatchScanEnabled(prefs.getBoolean("batch_scan", false));
        }
//        vm.getBatchScanEnabledLiveData().observe(getViewLifecycleOwner(), enabled -> {
//            binding.floatingTooltip.floatingTooltipRoot.setClickable(enabled);
//        });
//
//        vm.getNumberOfCodesScannedLiveData().observe(getViewLifecycleOwner(), num -> {
//            if (vm.getBatchScanEnabled()) {
//                binding.floatingTooltip.batchScanProceed.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
//
//                String tooltipText = num > 0 ? getString(R.string.codes_scanned, num) : getString(R.string.position_code);
//                binding.floatingTooltip.floatingTooltipDescription.setText(tooltipText);
//            }
//        });
//        binding.floatingTooltip.floatingTooltipRoot.setOnClickListener(v -> {
//            // Batch scan
//            if (vm.getBatchScanEnabled() && vm.getNumberOfCodesScanned() > 0) {
//                // Navigate to History
//                NavController navController = Navigation.findNavController(view);
//                navController.navigate(R.id.action_scannerFragment_to_historyFragment);
//            }
//        });


        com.team03.dtuevent.preferences.Settings globalSettings = com.team03.dtuevent.preferences.Settings.getInstance(getContext());

        // Request for permission only if the on-boarding is cleared.
        if (!globalSettings.getShouldShowOnboarding()) {
            int permissionStatus = requireContext().checkSelfPermission(cameraPermission);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

                startCamera();

            } else if (shouldShowRequestPermissionRationale(cameraPermission)) {
                displayRationale();
            } else {
                requestPm();
            }
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (vm.getCodes() == null) return;
        if (vm.getCodes().getValue() == null) return;

        for (ScanningWrapper s : vm.getCodes().getValue()) {
            s.dismissDialog();
        }
        binding = null;
        if (cameraShutdownCallback != null) {
            // Shutdown the camera if there is a callback, if not, most likely the camera is not even open anyway.
            cameraShutdownCallback.shutdown();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (camExecutor != null) {
            camExecutor.shutdown();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImg = data.getData();
            processImageUri(selectedImg);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions has been granted.
                startCamera();
            } else if (shouldShowRequestPermissionRationale(cameraPermission)) {
                displayRationale();
            } else if (requireContext().checkSelfPermission(cameraPermission) == PackageManager.PERMISSION_DENIED
                    && !shouldShowRequestPermissionRationale(cameraPermission)) {
                permissionDeniedPermanently(); // Permanent denial
            }
        }
    }

    private void processImageUri(Uri selectedImg) {
        if (selectedImg == null) {
            imageNotFound(getContext());
            return;
        }

        InputStream inputStream = openInputStream(selectedImg);
        if (inputStream == null) return;

        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        // Send bitmap to ML Kit image analyser
        processImage(inputStream, bmp, detected -> {
            if (!detected) {
                Snackbar.make(requireView(), R.string.no_codes_detected, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void processImage(InputStream inputStream, Bitmap bmp, Consumer<Boolean> detected) {

        InputImage inputImage = InputImage.fromBitmap(bmp, 0);
        barcodeScanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    vm.scanBarcodes(barcodes);
                    detected.accept(barcodes.size() > 0);
                })
                .addOnCompleteListener(task -> {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }


    private InputStream openInputStream(Uri selectedImg) {
        ContentResolver cr = requireActivity().getContentResolver();
        InputStream inputStream;
        try {
            inputStream = cr.openInputStream(selectedImg);
            if (inputStream == null) {
                imageNotFound(getContext());
                return null;
            }
        } catch (FileNotFoundException e) {
            imageNotFound(getContext());
            return null;
        }
        return inputStream;
    }

    private void imageNotFound(Context context) {
        Toast.makeText(context, R.string.img_not_found, Toast.LENGTH_SHORT).show();
    }


    private void flashToggle() {
        if (camAccessObj.getFlash() == Availability.ON) {
            camAccessObj.toggleFlash(Availability.OFF);
            //item.setIcon(R.drawable.ic_baseline_flash_off_24);
        } else {
            camAccessObj.toggleFlash(Availability.ON);
            //item.setIcon(R.drawable.ic_baseline_flash_on_24);
        }
    }


    public void analyzeFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, "image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void requestPm() {
        // Permission denied. Request the permission.
        requestPermissions(new String[]{cameraPermission}, CAMERA_REQUEST_CODE);
    }

    private void displayRationale() {
        new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_App_AlertDialogTheme)
                .setTitle(R.string.cam_pm)
                .setMessage(R.string.grant_pm_rationale)
                .setPositiveButton(R.string.grant, (dialog, which) -> requestPm())
                .setNegativeButton(R.string.cancel, (dialog, which) -> permissionDenied())
                .setCancelable(false)
                .create()
                .show();
    }

    private void permissionDenied() {

        Snackbar snack = Snackbar.make(binding.coordinatorLayout,
                        R.string.pm_denial, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.grant, v -> requestPm());

        setMargins(snack);
        snack.setBehavior(new DisableSwipeBehavior());

        snack.show();
    }

    private void permissionDeniedPermanently() {
        Snackbar snack = Snackbar.make(binding.previewView, R.string.pm_denial_permanant, Snackbar.LENGTH_INDEFINITE);
        snack.setAction(R.string.grant, v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri pkg = Uri.fromParts("package", requireContext().getPackageName(), null);
            intent.setData(pkg);
            startActivity(intent);
        });
        setMargins(snack);
        snack.show();
    }

    private void setMargins(Snackbar snack) {
        CoordinatorLayout.LayoutParams params = ((CoordinatorLayout.LayoutParams) snack.getView().getLayoutParams());
        if (requireActivity().findViewById(R.id.bottomNav) != null) {
            params.bottomMargin = requireActivity().findViewById(R.id.bottomNav).getHeight();
        }
        snack.getView().setLayoutParams(params);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void startCamera() {
        CameraFailureCallback cameraFailureCallback = cameraFailureDialog();
        SetTouchListenerCallback touchListenerCallback = listener -> binding.previewView.setOnTouchListener(listener);

        try {
            cameraShutdownCallback = camAccessObj.startCamera(getViewLifecycleOwner(), cameraFailureCallback, touchListenerCallback);

        } catch (ReferenceInvalidException e) {
            Log.e(TAG, "Context invalid.");
        }

    }


    @NonNull
    private CameraFailureCallback cameraFailureDialog() {
        return e -> Log.e(TAG, "Camera unavailable.");
    }

    private void noCamera() {
        AlertDialog cameraWarningDialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.no_cam)
                .setMessage(R.string.no_cam_msg)
                .create();

        cameraWarningDialog.show();
    }

    private static class DisableSwipeBehavior extends BaseTransientBottomBar.Behavior {
        @Override
        public boolean canSwipeDismissView(@NonNull View view) {
            return false;
        }
    }


}