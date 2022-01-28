package com.hangedfish.wasmedge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hangedfish.wasmedge.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'wasmedge' library on application startup.
    static {
        System.loadLibrary("wasmedge_android_app");
    }

    public native String stringFromJNI();

    public native int nativeWasmFib(byte wasm_bytes[], int idx);


    private ActivityMainBinding binding;

    private byte fib_wasm[];

    private void readWasm() {
        try {
            fib_wasm = readAsset(this, "fibonacci.wasm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readWasm();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fib_wasm == null) {
                    Toast.makeText(MainActivity.this, "read wasm fail", Toast.LENGTH_LONG).show();
                    return;
                }
                int idx = 24;
                int r = nativeWasmFib(fib_wasm, idx);
                tv.setText(String.format("fib(%d) -> %d", idx, r));
            }
        });
    }

    /**
     * A native method that is implemented by the 'wasmedge' native library,
     * which is packaged with this application.
     */


    public static byte[] readAsset(Context context, String filename)
            throws IOException {
        InputStream in = context.getResources().getAssets().open(filename);
        try {
            return readAllBytes(in);
        } finally {
            in.close();
        }
    }

    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyAllBytes(in, out);
        return out.toByteArray();
    }

    /**
     * Copies all available data from in to out without closing any stream.
     *
     * @return number of bytes copied
     */
    public static int copyAllBytes(InputStream in, OutputStream out)
            throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        return byteCount;
    }

}