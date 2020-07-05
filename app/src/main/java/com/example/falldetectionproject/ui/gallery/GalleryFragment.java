package com.example.falldetectionproject.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.falldetectionproject.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment implements View.OnClickListener {

    private GalleryViewModel galleryViewModel;
    public String FILE_NAME = "contact.txt";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        final Button goButton = root.findViewById(R.id.goButton);
        EditText regPhoneNo = root.findViewById(R.id.reg_phoneNo);
        final Button loadButton = root.findViewById(R.id.loadBtn);
        goButton.setOnClickListener(this);
        loadButton.setOnClickListener(this);


                //Intent i = new Intent(GalleryFragment.class, HomeFragment.class);
                //startActivity(i);


        /*galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
    void onClickLoad()
    {
        final String contact;
        FileInputStream fis = null;
        try {
            fis = getContext().openFileInput("contact.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            //setText(sb.toString());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }  finally {
            if(fis != null)
            {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.goButton:
                EditText regPhoneNo = (EditText) v.findViewById(R.id.reg_phoneNo);
                File file = new File(getContext().getFilesDir(), FILE_NAME);
                if(file.exists())
                {
                    getContext().deleteFile(FILE_NAME);
                }
                final String phone = regPhoneNo.getText().toString();
                FileOutputStream fos = null;
                try {
                    fos = getContext().openFileOutput(FILE_NAME, MODE_PRIVATE);
                    fos.write(phone.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if(fos != null)
                    {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                Toast toast = Toast.makeText(v.getContext(), phone, Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }
}