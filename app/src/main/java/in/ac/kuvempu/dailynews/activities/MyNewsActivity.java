package in.ac.kuvempu.dailynews.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import in.ac.kuvempu.dailynews.R;
import in.ac.kuvempu.dailynews.database.NewsDBHelper;
import in.ac.kuvempu.dailynews.model.ArticleItem;
import in.ac.kuvempu.dailynews.util.Utils;

public class MyNewsActivity extends AppCompatActivity {

    private EditText myNewsTitle;
    private EditText myNewsDesc;
    private EditText myNewsAuthor;
    private ImageView myNewsImg;
    private ImageView addImg;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private String capturedImagePath = "";
    private EditText myNewsPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_news);
        myNewsTitle = (EditText) findViewById(R.id.myNewsTitle);
        myNewsDesc = (EditText) findViewById(R.id.myNewsDesc);
        myNewsAuthor = (EditText) findViewById(R.id.myNewsAuthor);
        myNewsImg = (ImageView) findViewById(R.id.myNewsImg);
        addImg =  (ImageView) findViewById(R.id.addImg);
        myNewsPhoneNo = (EditText) findViewById(R.id.myNewsPhoneNo);
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_news_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.saveNews:
                if(checkAlltheFieldsEntered()) {
                    saveNewsToDB();
                    Toast.makeText(this, "News submitted...", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Please enter all the fields...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNewsToDB() {

        try {
            ArticleItem item = new ArticleItem();
            item.setSelf("X");
            item.setTitle(myNewsTitle.getText().toString());
            item.setDescription(myNewsDesc.getText().toString());
            item.setAuthor(myNewsAuthor.getText().toString());
            item.setPhoneNo(myNewsPhoneNo.getText().toString());
            item.setCategory("News Reported by Me");
            item.setUrlToImage(capturedImagePath);
            NewsDBHelper helper = new NewsDBHelper(this);

            if (helper != null) {
                helper.insertNews(item);
            }

            sendMailWithAttachment(item);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MyNewsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission(MyNewsActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        myNewsImg.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        capturedImagePath = destination.getPath();
        myNewsImg.setImageBitmap(thumbnail);
    }

    private void sendMailWithAttachment(ArticleItem item){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("image/jpeg");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]
                {"me@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, item.getTitle());
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, item.getDescription() + "\n\n" + "Regards\n" + item.getAuthor());
        Log.v(getClass().getSimpleName(), "sPhotoUri=" + Uri.parse("file://"+ capturedImagePath));
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+ capturedImagePath));
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

    }

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/DaileNews");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkAlltheFieldsEntered(){

        String title = myNewsTitle.getText().toString();
        String desc = myNewsDesc.getText().toString();
        String author = myNewsAuthor.getText().toString();
        boolean valid = true;
        if(title == null || title.trim().equals(""))
            valid = false;
        if(desc == null || desc.trim().equals(""))
            valid = false;
        if(author == null || author.trim().equals(""))
            valid = false;

        return valid;
    }
}