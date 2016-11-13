package algo.antomium.com.instapic;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
   // ActionBar bar = getActionBar();
   Uri imageUri;
    String strcheck;
    ImageView ivResult,ivGraysCale,ivHue,ivSepia,ivSepia2,ivBoost,ivInvert,ivTint;
   // ProgressBar progress;
    LinearLayout llFilter,llProgress;
    InputStream isImage=null;
    Bitmap myBitmap= null;
    TextView tvStatus;
    Toolbar mTool;
    Intent camIntent,intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTool=(Toolbar)findViewById(R.id.myToolBar);
        intent2=new Intent(this,Blank.class);


        ivResult=(ImageView)findViewById(R.id.ivResult);
        ivGraysCale=(ImageView)findViewById(R.id.ivGrayScale);
        ivSepia=(ImageView)findViewById(R.id.ivSepia);
        ivSepia2=(ImageView)findViewById(R.id.ivSepia2);
        ivBoost=(ImageView)findViewById(R.id.ivBoost);
        ivInvert=(ImageView)findViewById(R.id.ivInvert);
        ivTint=(ImageView)findViewById(R.id.ivTint);
        ivHue=(ImageView)findViewById(R.id.ivHue);
        tvStatus=(TextView)findViewById(R.id.tvStatus);

        Intent intent = getIntent();
        String action = intent.getAction();
         camIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        camIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,4000);
        String type = intent.getType();
      //  progress =(ProgressBar)findViewById(R.id.progressBar) ;
      //  progress.setVisibility(View.INVISIBLE);
        imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        //llFilter=(LinearLayout)findViewById(R.id.llFilter);
        //llProgress=(LinearLayout)findViewById(R.id.llLoad);

        try {
            isImage=getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bmCompressed = BitmapFactory.decodeStream(isImage);
        Bitmap resized = Bitmap.createScaledBitmap(bmCompressed,(int)(bmCompressed.getWidth()*0.1),(int)(bmCompressed.getHeight()*0.1),true);


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                try {
                    handleSendImage(intent); // Handle single image being sent
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                strcheck = imageUri.toString();
                if(strcheck!=null){
                   thumbView(resized);
                }
            }


        }
        else{
            startActivity(intent2);
        }
      


    }


    public void grayScaleClick(View view){
        tvStatus.setText("Applying filter");
        ivResult.setImageBitmap(imageEffects.doGreyscale(myBitmap));
        tvStatus.setText(" Filter Applied");
    }
    public void hueClick(View view){
        tvStatus.setText("Applying filter");
        ivResult.setImageBitmap(imageEffects.applyHueFilter(myBitmap,3));
        tvStatus.setText(" Filter Applied");
    }
    public void tintClick(View view){
        tvStatus.setText("Applying filter");
        ivResult.setImageBitmap(imageEffects.tintImage(myBitmap,50));
        tvStatus.setText(" Filter Applied");
    }
    public void invertClick(View view){
        tvStatus.setText("Applying filter");
        ivResult.setImageBitmap(imageEffects.doInvert(myBitmap));
        tvStatus.setText(" Filter Applied");
    }
    public void boostClick(View view){
        tvStatus.setText("Applying filter");
        ivResult.setImageBitmap(imageEffects.boost(myBitmap,3,67f));
        tvStatus.setText(" Filter Applied");
    }
    public void sepiaClick(View view){
        tvStatus.setText("Applying filter");
        ivResult.setImageBitmap(imageEffects.createSepiaToningEffect(myBitmap,50,0.0,25.0,0.0));
        tvStatus.setText(" Filter Applied");
    }
    public void sepia2Click(View view){
        tvStatus.setText("Applying filter");
        ivResult.setImageBitmap(imageEffects.createSepiaToningEffect(myBitmap,50,25.0,0.0,0.0));
        tvStatus.setText(" Filter Applied");
    }
    public void loadCam(View view){
        startActivityForResult(camIntent,0);
    }

    public void thumbView(Bitmap resized){

        //  llFilter.setVisibility(View.INVISIBLE);
        //  progress.setVisibility(View.VISIBLE);

        ivGraysCale.setImageBitmap(imageEffects.doGreyscale(resized));
        ivHue.setImageBitmap(imageEffects.applyHueFilter(resized,3));
        ivTint.setImageBitmap(imageEffects.tintImage(resized,50));
        ivInvert.setImageBitmap(imageEffects.doInvert(resized));
        ivBoost.setImageBitmap(imageEffects.boost(resized,3,67f));
        ivSepia.setImageBitmap(imageEffects.createSepiaToningEffect(resized,50,0.0,25.0,0.0));
        ivSepia2.setImageBitmap(imageEffects.createSepiaToningEffect(resized,50,25.0,0.0,0.0));
        // llFilter.setVisibility(View.VISIBLE);
        // progress.setVisibility(View.GONE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        ivResult.setImageBitmap(bp);
        myBitmap=bp;
        Bitmap bpResized =  Bitmap.createScaledBitmap(bp,(int)(bp.getWidth()*0.1),(int)(bp.getHeight()*0.1),true);
        thumbView(bpResized);

    }







    void handleSendImage(Intent intent) throws IOException {

        if (imageUri != null) {
            ivResult.setImageURI(imageUri);

        }
    }
}
