public class Response {
	//Biaz is a fucking loser
	public byte ID;
	public byte errorCode;
	public final byte total_length = 7;
	public int result;

	public Response(byte in_ID, byte in_Err, int in_Result) {
		this.ID = in_ID;
		this.errorCode = in_Err;
		this.result = in_Result;
	}



}
