package com.blackbook.survey.asynktask;

import android.content.Context;
import android.os.AsyncTask;
import androidx.fragment.app.Fragment;

import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.interfaces.WsResponseListener;
import com.blackbook.survey.model.Common;
import com.blackbook.survey.model.ResponseResult;
import com.blackbook.survey.ws.RequestWs;
import com.google.gson.Gson;

import org.json.JSONObject;


public class AsyncPostService extends AsyncTask<String, Void, Object> {

	private Context mContext;
    private String serviceType;
    private String loadingmsg;

    private WsResponseListener wsResponseListener;

    private boolean isloaderEnable;
    private boolean passObject;
    private Common commonObj;

    private Exception error = null;

	public AsyncPostService(Context mContext, String loadingmsg, String serviceType,boolean isloaderEnable,boolean passObject)
	{

		this.mContext = mContext;
		this.loadingmsg = loadingmsg;
		this.serviceType = serviceType;
		this.isloaderEnable = isloaderEnable;
		this.passObject = passObject;

		wsResponseListener = (WsResponseListener) mContext;

	}

	public AsyncPostService(Context mContext, String loadingmsg, String serviceType, Common commonObj, boolean isloaderEnable,boolean passObject)
	{

		this.mContext = mContext;
		this.loadingmsg = loadingmsg;
		this.serviceType = serviceType;
		this.commonObj = commonObj;
		this.isloaderEnable = isloaderEnable;
		this.passObject = passObject;

		wsResponseListener = (WsResponseListener) mContext;

	}

	public AsyncPostService(Context mContext, String loadingmsg,Fragment fragment,String serviceType, Common commonObj,boolean isloaderEnable,boolean passObject)
	{

		this.mContext = mContext;
		this.loadingmsg = loadingmsg;
		this.serviceType = serviceType;
		this.commonObj = commonObj;
		this.isloaderEnable = isloaderEnable;
		this.passObject = passObject;

		wsResponseListener = (WsResponseListener) fragment;

	}

	@Override
	protected void onPreExecute() 
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (isloaderEnable)
			AppGlobal.showProgressDialog(mContext, loadingmsg);
	}

	@Override
	protected Object doInBackground(String... params)
	{
 		try
		{
			if(passObject)
			{
				Gson gson = new Gson();
				String json = gson.toJson(commonObj);
				JSONObject obj = new JSONObject(json);

				return new RequestWs().getPostRequest(params[0], obj, ResponseResult.class);
			}
			else
			{
				return new RequestWs().getPostRequest(params[0], ResponseResult.class);
			}

		}
		catch (Exception e)
		{
			error = e;
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (isloaderEnable)
			AppGlobal.hideProgressDialog(mContext);
		wsResponseListener.onDelieverResponse(serviceType, result, error);
	}

}
