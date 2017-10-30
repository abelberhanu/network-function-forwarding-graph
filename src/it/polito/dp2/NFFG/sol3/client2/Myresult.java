package it.polito.dp2.NFFG.sol3.client2;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

public class Myresult implements VerificationResultReader {


	PolicyReader policy;
	Boolean result;
	String message;
	 Calendar updatetime = Calendar.getInstance();
	static DateFormat dateFormat;
	
	
	public Myresult(PolicyReader policy,String message,Calendar upd,Boolean res) {
			this.policy=policy;
			this.message=message;
			this.result=res;
			this.updatetime=upd;

		}

	public Myresult(PolicyReader pname, Boolean result2, String msg,
			GregorianCalendar gregorianCalendar) {
		this.policy= pname;
		this.result=result2;
		this.message=msg;
		this.updatetime=gregorianCalendar;
	}



	@Override
	public PolicyReader getPolicy() {
		// TODO Auto-generated method stub
		return this.policy;
	}

	@Override
	public Boolean getVerificationResult() {
		// TODO Auto-generated method stub
		return this.result;
	}

	@Override
	public String getVerificationResultMsg() {
		// TODO Auto-generated method stub
		return this.message;
	}

	@Override
	public Calendar getVerificationTime() {
		// TODO Auto-generated method stub
		return this.updatetime;
	}
}
