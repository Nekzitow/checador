/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Clases;

import com.sun.jna.Callback;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCall;
import com.sun.jna.win32.StdCallLibrary;
//import com.suprema.ufe31.UFScannerClass.UFS_CAPTURE_PROC;
//import com.suprema.ufe31.UFScannerClass.UFS_SCANNER_PROC;

public interface UFScannerClass extends StdCallLibrary {

    public static interface UFS_SCANNER_PROC extends Callback, StdCall {

        public int callback(String string, int i, PointerByReference pbr);
    }

    public static interface UFS_CAPTURE_PROC extends Callback {

        public int callback(Pointer pntr, int i, Pointer pntr1, int i1, int i2, int i3, PointerByReference pbr);
    }
    public static final int UFS_OK = 0;
    public static final int UFS_STATUS = 0;
    public static final int UFS_ERROR = -1;
    public static final int UFS_ERR_NO_LICENSE = -101;
    public static final int UFS_ERR_LICENSE_NOT_MATCH = -102;
    public static final int UFS_ERR_LICENSE_EXPIRED = -103;
    public static final int UFS_ERR_NOT_SUPPORTED = -111;
    public static final int UFS_ERR_INVALID_PARAMETERS = -112;
    public static final int UFS_ERR_ALREADY_INITIALIZED = -201;
    public static final int UFS_ERR_NOT_INITIALIZED = -202;
    public static final int UFS_ERR_DEVICE_NUMBER_EXCEED = -203;
    public static final int UFS_ERR_LOAD_SCANNER_LIBRARY = -204;
    public static final int UFS_ERR_CAPTURE_RUNNING = -211;
    public static final int UFS_ERR_CAPTURE_FAILED = -212;
    public static final int UFS_ERR_NOT_GOOD_IMAGE = -301;
    public static final int UFS_ERR_EXTRACTION_FAILED = -302;
    public static final int UFS_ERR_CORE_NOT_DETECTED = -351;
    public static final int UFS_ERR_CORE_TO_LEFT = -352;
    public static final int UFS_ERR_CORE_TO_LEFT_TOP = -353;
    public static final int UFS_ERR_CORE_TO_TOP = -354;
    public static final int UFS_ERR_CORE_TO_RIGHT_TOP = -355;
    public static final int UFS_ERR_CORE_TO_RIGHT = -356;
    public static final int UFS_ERR_CORE_TO_RIGHT_BOTTOM = -357;
    public static final int UFS_ERR_CORE_TO_BOTTOM = -358;
    public static final int UFS_ERR_CORE_TO_LEFT_BOTTOM = -359;
    public static final int UFS_PARAM_TIMEOUT = 201;
    public static final int UFS_PARAM_BRIGHTNESS = 202;
    public static final int UFS_PARAM_SENSITIVITY = 203;
    public static final int UFS_PARAM_SERIAL = 204;
    public static final int UFS_PARAM_DETECT_CORE = 301;
    public static final int UFS_PARAM_TEMPLATE_SIZE = 302;
    public static final int UFS_PARAM_USE_SIF = 311;
    public static final int UFS_SCANNER_TYPE_SFR200 = 1001;
    public static final int UFS_SCANNER_TYPE_SFR300 = 1002;
    public static final int UFS_SCANNER_TYPE_SFR300v2 = 1003;
    public static final int UFS_SCANNER_TYPE_SFR400 = 1004;
    public static final int UFS_TEMPLATE_TYPE_SUPREMA = 2001;
    public static final int UFS_TEMPLATE_TYPE_ISO19794_2 = 2002;
    public static final int UFS_TEMPLATE_TYPE_ANSI378 = 2003;

    public int UFS_Init();

    public int UFS_Update();

    public int UFS_Uninit();

    public int UFS_SetScannerCallback(UFS_SCANNER_PROC u, PointerByReference pbr);

    public int UFS_RemoveScannerCallback();

    public int UFS_GetScannerNumber(IntByReference ibr);

    public int UFS_GetScannerHandle(int i, PointerByReference pbr);

    public int UFS_GetScannerHandleByID(String string, PointerByReference pbr);

    public int UFS_GetScannerIndex(Pointer pntr, IntByReference ibr);

    public int UFS_GetScannerID(Pointer pntr, byte[] bytes);

    public int UFS_GetScannerType(Pointer pntr, IntByReference ibr);

    public int UFS_GetParameter(Pointer pntr, int i, IntByReference ibr);

    public int UFS_SetParameter(Pointer pntr, int i, IntByReference ibr);

    public int UFS_IsSensorOn(Pointer pntr, IntByReference ibr);

    public int UFS_IsFingerOn(Pointer pntr, IntByReference ibr);

    public int UFS_CaptureSingleImage(Pointer pntr);

    public int UFS_StartCapturing(Pointer pntr, UFS_CAPTURE_PROC u, PointerByReference pbr);

    public int UFS_IsCapturing(Pointer pntr, IntByReference ibr);

    public int UFS_AbortCapturing(Pointer pntr);

    public int UFS_Extract(Pointer pntr, byte[] bytes, IntByReference ibr, IntByReference ibr1);

    public int UFS_SetEncryptionKey(Pointer pntr, String string);

    public int UFS_EncryptTemplate(Pointer pntr, byte[] bytes, int i, byte[] bytes1, IntByReference ibr);

    public int UFS_DecryptTemplate(Pointer pntr, byte[] bytes, int i, byte[] bytes1, IntByReference ibr);

    public int UFS_GetCaptureImageBufferInfo(Pointer pntr, IntByReference ibr, IntByReference ibr1, IntByReference ibr2);

    public int UFS_GetCaptureImageBuffer(Pointer pntr, byte[] bytes);

    public int UFS_SaveCaptureImageBufferToBMP(Pointer pntr, String string);

    public int UFS_ClearCaptureImageBuffer(Pointer pntr);

    public int UFS_GetErrorString(int i, byte[] bytes);

    public int UFS_GetTemplateType(Pointer pntr, IntByReference ibr);

    public int UFS_SetTemplateType(Pointer pntr, int i);
}
