package com.lunadeveloper.codered;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;


public class SettingsActivity extends ActionBarActivity {

    public Button ImageUpload;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ImageUpload = (Button) findViewById(R.id.imageUpload);

        ImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap imageScaled = Bitmap.createScaledBitmap(bitmap, 200, 200
                        * bitmap.getHeight() / bitmap.getWidth(), false);

                // Override Android default landscape orientation and save portrait
                Matrix matrix = new Matrix();
                matrix.postRotate(-90);
                Bitmap rotatedScaledImage = Bitmap.createBitmap(imageScaled, 0,
                        0, imageScaled.getWidth(), imageScaled.getHeight(),
                        matrix, true);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                rotatedScaledImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                byte[] scaledData = bos.toByteArray();

                // always happens
                System.out.println("SIZE: "+scaledData.length);
                final ParseFile pf = new ParseFile("user.png", scaledData);
                pf.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            ParseUser.getCurrentUser().put("image", pf);
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null) {
                                        Toast.makeText(getApplicationContext(), "Profile image saved", Toast.LENGTH_SHORT).show();
                                    } else {
                                        System.out.println("EAVING ERROR: "+e.getMessage());
                                    }
                                }
                            });
                        } else {
                            System.out.println("FILE SAVING FAILED" + e.getMessage());
                        }
                    }
                });

                ImageView imageView = (ImageView) findViewById(R.id.userImage);
                imageView.setImageBitmap(rotatedScaledImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
