package com.example.pdf_create;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btn_create;
    Bitmap bitmap,scaledbitmap;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_create = (Button)findViewById(R.id.btn_create);

        //WRITE_EXTERNAL_STORAGE 권한 요청
        ActivityCompat.requestPermissions(MainActivity.this,new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        // 파일, 스트림 및 바이트 배열을 포함한 다양한 원본에서 비트맵 개체를 만듭니다.

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mountain312);
        //이미지의 resize를 가능케합니다.
        scaledbitmap = Bitmap.createScaledBitmap(bitmap,600,300,true);

        CreatePDF();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void CreatePDF(){

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 새 문서 생성
                PdfDocument pdfDocument = new PdfDocument();
                // Paint는 텍스트 및 비트맵을 그리는 방법에 대한 스타일 및 색상 정보를 보관합니다.
                Paint paint = new Paint();
                Paint Paint_Image = new Paint();

                // 페이지 설명을 추가.
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(885,1080,1).create();
                // 페이지 시작
                PdfDocument.Page mypage = pdfDocument.startPage(pageInfo);

                Canvas canvas = mypage.getCanvas();
                canvas.drawText("PDF Create",150,50,paint);

                //페이지 종료
                pdfDocument.finishPage(mypage);

                // 2번째 페이지 생성
                PdfDocument.PageInfo pageInfo2 = new PdfDocument.PageInfo.Builder(885,1080,1).create();
                PdfDocument.Page mypage2 = pdfDocument.startPage(pageInfo2);
                Canvas canvas2 = mypage2.getCanvas();
                // 78번째 줄에서 Bitmap.isrecycle()이 null을 반환하여 오류가 생길경우 44번째 줄의 drawable 를 다시 확인해주세요.
                // bitmap이 아닌 scaleBitmap을 사용한다면 이미지의 resize가 가능합니다.
                canvas2.drawBitmap(scaledbitmap,150,300,Paint_Image);
                pdfDocument.finishPage(mypage2);

                //file을 생성.
                //Environment.getExternalStorageDirectory() 메소드는 API 29 이상에선 사용할 수 없습니다.
                //Manifest <Application 밑에 android:requestLegacyExternalStorage="true" 를 추가해야합니다.
                //또는 MediaStore 을 이용해야합니다.
                File file = new File(Environment.getExternalStorageDirectory(),"/PDF CREATE TEST.pdf");

                try {
                    pdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pdfDocument.close();
               }


        });
    }
}