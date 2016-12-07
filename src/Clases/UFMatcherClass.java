package Clases;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


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
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface UFMatcherClass extends StdCallLibrary {

    public static final int UFMOK = 0;
    public static final int UFMERROR = -1;
    public static final int UFMERR_NO_LICENSE = -101;
    public static final int UFMERR_LICENSE_NOT_MATCH = -102;
    public static final int UFMERR_LICENSE_EXPIRED = -103;
    public static final int UFMERR_NOT_SUPPORTED = -111;
    public static final int UFMERR_INVALID_PARAMETERS = -112;
    public static final int UFM_ERR_MATCH_TIMEOUT = -401;
    public static final int UFM_ERR_MATCH_ABORTED = -402;
    public static final int UFM_ERR_TEMPLATE_TYPE = -411;
    public static final int UFM_PARAM_FAST_MODE = 301;
    public static final int UFM_PARAM_SECURITY_LEVEL = 302;
    public static final int UFM_PARAM_USE_SIF = 311;
    public static final int UFM_TEMPLATE_TYPE_SUPREMA = 2001;
    public static final int UFM_TEMPLATE_TYPE_ISO19794_2 = 2002;
    public static final int UFM_TEMPLATE_TYPE_ANSI378 = 2003;

    public int UFM_Create(PointerByReference pbr);

    public int UFM_Delete(Pointer pntr);

    public int UFM_GetParameter(Pointer pntr, int i, IntByReference ibr);

    public int UFM_SetParameter(Pointer pntr, int i, IntByReference ibr);

    public int UFM_Verify(Pointer pntr, byte[] bytes, int i, byte[] bytes1, int i1, IntByReference ibr);

    public int UFM_Identify(Pointer pntr, byte[] bytes, int i, PointerByReference pbr, int[] ints, int i1, int i2, IntByReference ibr);

    public int UFM_IdentifyMT(Pointer pntr, byte[] bytes, int i, PointerByReference pbr, int[] ints, int i1, int i2, IntByReference ibr);

    public int UFM_AbortIdentify(Pointer pntr);

    public int UFM_IdentifyInit(Pointer pntr, byte[] bytes, int i);

    public int UFM_IdentifyNext(Pointer pntr, byte[] bytes, int i, IntByReference ibr);

    public int UFM_RotateTemplate(Pointer pntr, byte[] bytes, int i);

    public int UFM_GetErrorString(int i, byte[] bytes);

    public int UFM_GetTemplateType(Pointer pntr, IntByReference ibr);

    public int UFM_SetTemplateType(Pointer pntr, int i);
}
