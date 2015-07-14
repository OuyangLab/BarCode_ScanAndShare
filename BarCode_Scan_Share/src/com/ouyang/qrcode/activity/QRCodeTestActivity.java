package com.ouyang.qrcode.activity;

import java.io.File;


import com.google.zxing.WriterException;
import com.ouyang.R;
import com.ouyang.share.AndroidShare;
import com.ouyang.util.ImageUtils;


import com.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QRCodeTestActivity extends Activity {
	/** Called when the activity is first created. */
	private TextView resultTextView;
	private EditText qrStrEditText;
	private ImageView qrImgImageView;
	private Button qrShare;
	private String path;
	private String filepath;
	private Bitmap qrCodeBitmap;
	private Button scanBarCodeButton;
	private Button generateQRCodeButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
		initEvent();

	}

	private void initEvent() {
		
		scanBarCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent openCameraIntent = new Intent(QRCodeTestActivity.this,
						MyCaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});

		generateQRCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String contentString = qrStrEditText.getText().toString();
					if (!contentString.equals("")) {

						qrCodeBitmap = EncodingHandler.createQRCode(
								contentString, 350);
						
						qrImgImageView.setImageBitmap(qrCodeBitmap);

					} else {
						Toast.makeText(QRCodeTestActivity.this,
								"Text can not be empty", Toast.LENGTH_SHORT)
								.show();
					}

				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		qrShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				filepath = com.ouyang.util.FileUtils.getSDPath()
						+ File.separator + "image" + File.separator;
				if(qrCodeBitmap!=null ){					
					path = ImageUtils.savePhotoToSDCard(qrCodeBitmap, filepath,
							"QRCode.jpg");
					AndroidShare as = new AndroidShare(QRCodeTestActivity.this,
							null, path);
					as.show();
				}else{
					Toast.makeText(QRCodeTestActivity.this, "Please create QRCode image first!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

	}

	private void initView() {
		resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
		qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
		qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
		qrShare = (Button) findViewById(R.id.bt_share);
		scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
		generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描结果回调
		switch(requestCode){
		case 0:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				resultTextView.setText(scanResult);
			}
		}
		
	
	}
}