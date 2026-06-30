package com.florabreak.app.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.florabreak.app.R;
import com.florabreak.app.data.repository.BreakSessionRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Echte Kamera-Funktion für den Streckenbeweis.
 *
 * Das Foto wird:
 * - in app-internem Speicher abgelegt
 * - komprimiert
 * - mit der aktuellen Pause in der Room-Datenbank verknüpft
 */
public class RouteProofActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 51;
    private static final int CAMERA_REQUEST_CODE = 52;

    private TextView backFromProofButton;
    private Button captureProofButton;
    private ImageView proofImagePreview;
    private TextView proofStatusText;
    private TextView proofPathText;

    private BreakSessionRepository breakSessionRepository;

    private long breakSessionId = -1L;
    private String currentPhotoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_proof);

        breakSessionRepository = new BreakSessionRepository(this);

        breakSessionId = getIntent().getLongExtra("breakSessionId", -1L);

        bindViews();
        setupButtons();
    }

    private void bindViews() {
        backFromProofButton = findViewById(R.id.backFromProofButton);
        captureProofButton = findViewById(R.id.captureProofButton);
        proofImagePreview = findViewById(R.id.proofImagePreview);
        proofStatusText = findViewById(R.id.proofStatusText);
        proofPathText = findViewById(R.id.proofPathText);
    }

    private void setupButtons() {
        backFromProofButton.setOnClickListener(view -> finish());

        captureProofButton.setOnClickListener(view -> {
            if (breakSessionId <= 0) {
                Toast.makeText(
                        this,
                        "Keine aktive Pause gefunden. Foto kann nicht gespeichert werden.",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            openCameraWithPermission();
        });
    }

    private void openCameraWithPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE
            );
            return;
        }

        openCamera();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Keine Kamera-App verfügbar", Toast.LENGTH_SHORT).show();
            return;
        }

        File photoFile;

        try {
            photoFile = createPhotoFile();
        } catch (IOException exception) {
            Toast.makeText(this, "Fotodatei konnte nicht erstellt werden", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri photoUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider",
                photoFile
        );

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private File createPhotoFile() throws IOException {
        File directory = new File(getFilesDir(), "photo_proofs");

        if (!directory.exists()) {
            boolean created = directory.mkdirs();

            if (!created) {
                throw new IOException("Photo proof directory could not be created");
            }
        }

        String fileName = "proof_break_"
                + breakSessionId
                + "_"
                + System.currentTimeMillis()
                + ".jpg";

        File photoFile = new File(directory, fileName);
        currentPhotoPath = photoFile.getAbsolutePath();

        return photoFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                handleCapturedPhoto();
            } else {
                Toast.makeText(this, "Fotoaufnahme abgebrochen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleCapturedPhoto() {
        if (currentPhotoPath == null || currentPhotoPath.trim().isEmpty()) {
            Toast.makeText(this, "Kein Foto-Pfad vorhanden", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean compressed = compressPhoto(currentPhotoPath);

        if (!compressed) {
            Toast.makeText(this, "Foto konnte nicht komprimiert werden", Toast.LENGTH_SHORT).show();
            return;
        }

        breakSessionRepository.savePhotoProof(breakSessionId, currentPhotoPath);

        Bitmap previewBitmap = BitmapFactory.decodeFile(currentPhotoPath);
        proofImagePreview.setImageBitmap(previewBitmap);

        proofStatusText.setText("Foto-Beweis gespeichert");
        proofPathText.setText("Lokal gespeichert und mit Pause #" + breakSessionId + " verknüpft.");

        Toast.makeText(this, "Streckenbeweis gespeichert", Toast.LENGTH_SHORT).show();
    }

    private boolean compressPhoto(String photoPath) {
        Bitmap originalBitmap = BitmapFactory.decodeFile(photoPath);

        if (originalBitmap == null) {
            return false;
        }

        Bitmap scaledBitmap = scaleBitmap(originalBitmap, 1280);

        try (FileOutputStream outputStream = new FileOutputStream(photoPath)) {
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
            outputStream.flush();
        } catch (IOException exception) {
            return false;
        } finally {
            if (scaledBitmap != originalBitmap) {
                scaledBitmap.recycle();
            }

            originalBitmap.recycle();
        }

        return true;
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxSize && height <= maxSize) {
            return bitmap;
        }

        float ratio = Math.min(
                (float) maxSize / width,
                (float) maxSize / height
        );

        int newWidth = Math.round(width * ratio);
        int newHeight = Math.round(height * ratio);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(
                        this,
                        "Kamera-Berechtigung fehlt. Streckenbeweis nicht möglich.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
