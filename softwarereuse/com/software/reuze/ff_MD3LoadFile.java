package com.software.reuze;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;



public class ff_MD3LoadFile {
///////////////////////////////// IMPORT MD3 \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
	/////
	/////	This is called by the client to open the .Md3 file, read it, then clean up
	/////
	///////////////////////////////// IMPORT MD3 \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\*
	public Object ImportMD3(String path) {

		// Open the MD3 file in binary
		f_LittleEndianDataInputStream f;
		try {
			f = new f_LittleEndianDataInputStream(new FileInputStream(path));
		} catch (FileNotFoundException e1) {
			return null;
		}
/*		
		// Read the header data and store it in our m_Header member variable
		fread(&m_Header, 1, sizeof(tMd3Header), m_FilePointer);

		// Get the 4 character ID
		char *ID = m_Header.fileID;

		// The ID MUST equal "IDP3" and the version MUST be 15, or else it isn't a valid
		// .MD3 file.  This is just the numbers ID Software chose.

		// Make sure the ID == IDP3 and the version is this crazy number '15' or else it's a bad egg.
		if((ID[0] != 'I' || ID[1] != 'D' || ID[2] != 'P' || ID[3] != '3') || m_Header.version != 15)
		{
			// Display an error message for bad file format, then stop loading
			fclose(m_FilePointer);
			return false;
		}
		
		// Read in the model and animation data
		ReadMD3Data(pModel);

		// Clean up after everything
		CleanUp();

		// Return a success
		return true;
*/
		try {
			f.close();
		} catch (IOException e) {
			return null;
		}
		return null;
	}

}
