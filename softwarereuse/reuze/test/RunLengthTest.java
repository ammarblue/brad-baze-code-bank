package reuze.test;
/* Run Length Test 
 * CSCI 7130
 * Michael L. Grecol ID 900756516
 * 8/25/12
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import com.software.reuze.cc_RunLength;
import com.software.reuze.f_StdInBits;
import com.software.reuze.f_StdOutBits;


public class RunLengthTest extends TestCase {
	private static byte[] TESTBYTES;
	private static byte[] OUTBYTES;
	private static ByteArrayInputStream S_in;
	private static ByteArrayOutputStream S_out;

	@Test
	public void testRunLengthCompress() throws IOException {
		TESTBYTES = new byte[5];
		TESTBYTES[0] = (byte) Integer.parseInt("00000000", 2);
		TESTBYTES[1] = (byte) Integer.parseInt("00000001", 2);
		TESTBYTES[2] = (byte) Integer.parseInt("11111100", 2);
		TESTBYTES[3] = (byte) Integer.parseInt("00000111", 2);
		TESTBYTES[4] = (byte) Integer.parseInt("11111111", 2);
		InputStream stdin = System.in;
		PrintStream stdout = System.out;
		S_out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(S_out));
		S_in = new ByteArrayInputStream(TESTBYTES);
		System.setIn(S_in);
		cc_RunLength.main(new String[] { "-" });
		OUTBYTES = S_out.toByteArray();
		S_in.close();
		S_out.flush();
		S_out.close();
		Assert.assertArrayEquals(new byte[] {(byte)15,(byte)7,(byte)7, (byte)11}, OUTBYTES);
	}

	@Test
	public void testRunLengthExpand() throws IOException {
		S_out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(S_out));
		S_in = new ByteArrayInputStream(OUTBYTES);
		System.setIn(S_in);
		f_StdInBits.open();
		f_StdOutBits.open();
		cc_RunLength.main(new String[] { "+" });
		byte[] EXPANDEDBYTES;
		EXPANDEDBYTES = S_out.toByteArray();
		S_in.close();
		S_out.close();
		Assert.assertArrayEquals(TESTBYTES, EXPANDEDBYTES);
	}
}
