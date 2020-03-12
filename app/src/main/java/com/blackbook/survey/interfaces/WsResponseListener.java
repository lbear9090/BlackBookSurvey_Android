/**
 * @author c61
 * 
 *Single Listener for All web services call..
 *This is Interface, used for All the web services Response.
 *We can impliment this to get response of web services with any activity or fragment.
 */

package com.blackbook.survey.interfaces;

public interface WsResponseListener {

	/**
	 * @param serviceType
	 * @param data
	 * @param error
	 */
	abstract void onDelieverResponse(String serviceType, Object data,
									 Exception error);

}
