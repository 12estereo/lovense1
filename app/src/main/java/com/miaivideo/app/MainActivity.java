package com.miaivideo.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int PICK_IMAGE = 41;
    private String mode = "Texto a video";
    private Uri selectedImage;
    private TextView imageStatus;

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView text(String value, int size, int color) {
        TextView v = new TextView(this);
        v.setText(value);
        v.setTextSize(size);
        v.setTextColor(color);
        return v;
    }

    private Button button(String value) {
        Button b = new Button(this);
        b.setText(value);
        b.setAllCaps(false);
        return b;
    }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(16, 16, 26));
        getWindow().setNavigationBarColor(Color.rgb(16, 16, 26));

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(16, 16, 26));
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(28), dp(22), dp(28));
        scroll.addView(page);

        TextView title = text("MI AI VIDEO", 30, Color.WHITE);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);
        page.addView(title);
        TextView subtitle = text("Crea videos con inteligencia artificial", 15, Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(-1, -2);
        subParams.setMargins(0, dp(6), 0, dp(24));
        page.addView(subtitle, subParams);

        TextView modeLabel = text("TIPO DE CREACIÓN", 13, 0xFFB9A7FF);
        page.addView(modeLabel);
        LinearLayout modes = new LinearLayout(this);
        modes.setOrientation(LinearLayout.HORIZONTAL);
        Button textMode = button("Texto a video");
        Button imageMode = button("Imagen a video");
        modes.addView(textMode, new LinearLayout.LayoutParams(0, -2, 1));
        modes.addView(imageMode, new LinearLayout.LayoutParams(0, -2, 1));
        page.addView(modes);
        textMode.setOnClickListener(v -> mode = "Texto a video");
        imageMode.setOnClickListener(v -> mode = "Imagen a video");

        TextView promptLabel = text("DESCRIBE EL VIDEO QUE QUIERES", 13, 0xFFB9A7FF);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(-1, -2);
        labelParams.setMargins(0, dp(22), 0, dp(8));
        page.addView(promptLabel, labelParams);
        EditText prompt = new EditText(this);
        prompt.setHint("Ejemplo: Un paisaje colombiano al atardecer, cámara cinematográfica...");
        prompt.setTextColor(Color.WHITE);
        prompt.setHintTextColor(0xFF9999A8);
        prompt.setMinLines(4);
        prompt.setGravity(Gravity.TOP | Gravity.START);
        prompt.setPadding(dp(12), dp(12), dp(12), dp(12));
        prompt.setBackgroundColor(0xFF29283A);
        page.addView(prompt, new LinearLayout.LayoutParams(-1, dp(126)));

        Button choose = button("＋  Elegir imagen (opcional)");
        LinearLayout.LayoutParams chooseParams = new LinearLayout.LayoutParams(-1, -2);
        chooseParams.setMargins(0, dp(14), 0, dp(4));
        page.addView(choose, chooseParams);
        imageStatus = text("Ninguna imagen seleccionada", 12, Color.LTGRAY);
        page.addView(imageStatus);
        choose.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.setType("image/*");
            i.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(i, PICK_IMAGE);
        });

        TextView settings = text("CONFIGURACIÓN", 13, 0xFFB9A7FF);
        LinearLayout.LayoutParams settingsParams = new LinearLayout.LayoutParams(-1, -2);
        settingsParams.setMargins(0, dp(22), 0, dp(8));
        page.addView(settings, settingsParams);
        TextView quality = text("Calidad: 720p HD\nFormato: Vertical 9:16\nDuración inicial: 5 segundos", 15, Color.WHITE);
        quality.setPadding(dp(14), dp(14), dp(14), dp(14));
        quality.setBackgroundColor(0xFF29283A);
        page.addView(quality);

        Button generate = button("PREPARAR GENERACIÓN");
        generate.setTextColor(Color.WHITE);
        generate.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF7047D7));
        LinearLayout.LayoutParams genParams = new LinearLayout.LayoutParams(-1, dp(56));
        genParams.setMargins(0, dp(24), 0, dp(12));
        page.addView(generate, genParams);
        generate.setOnClickListener(v -> {
            String description = prompt.getText().toString().trim();
            if (description.isEmpty()) {
                prompt.setError("Escribe una descripción para tu video");
                return;
            }
            new AlertDialog.Builder(this)
                .setTitle("Preparación lista")
                .setMessage("Modo: " + mode + "\nCalidad: 720p vertical\n\nLa interfaz está preparada, pero todavía falta conectar y probar un proveedor de generación de video con IA. Este botón aún no crea un MP4.")
                .setPositiveButton("Entendido", null)
                .show();
        });
        TextView note = text("Versión de prueba 0.2 · La generación real se conectará en una siguiente etapa.", 12, 0xFF9C9CAD);
        note.setGravity(Gravity.CENTER);
        page.addView(note);
        setContentView(scroll);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            if (selectedImage != null) {
                try { getContentResolver().takePersistableUriPermission(selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION); }
                catch (Exception ignored) { }
                imageStatus.setText("Imagen seleccionada correctamente");
            }
        }
    }
}
