package com.cw.netnfcreadidcardlib;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.util.Log;



public abstract class BaseNFCActivity extends Activity {
	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mIntentFilters;
	private String[][] mTechLists;
	public ReadAPI api;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		//mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);

		mPendingIntent = PendingIntent.getActivity(this, 0, (new Intent(this, this.getClass())).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		mIntentFilters = new IntentFilter[] { intentFilter };
		mTechLists = new String[][] { new String[] { NfcB.class.getName() } };
	}

	@Override
	protected void onResume() {
		super.onResume();

		mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mTechLists);
	}



	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			if (api != null) {
				api.read(intent);
			} else {
				Log.d(Constants.TAG, "api 未初始化");
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}