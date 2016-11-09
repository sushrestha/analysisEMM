package memphis.emm.bypassemm;

/**
 * Created by sujit on 10/27/2016.
 */


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;


import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Xposed implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparm) throws Throwable {

        String wso2PackageName = "org.wso2.emm.agent";
        if (lpparm.packageName.equals(wso2PackageName)){
            XposedBridge.log("WSO2 emm agent "+lpparm.packageName+" loaded.");

            //prevent lock device
             preventOperation(lpparm, "lockDevice");
            //prevent wipe_device
             preventOperation(lpparm,"wipeDevice");

            //detect and hook before calling the method
            detectAndHookBefore(lpparm,"getMessages");

            //change location if apply
//            changeLocation(lpparm);

        }



    }

    /* Prevent Operation based on method name */
    public void preventOperation(LoadPackageParam lpparam, final String methodName){


        String classToHook = lpparam.packageName+".services.Operation";
        String functionToHook = methodName;
        XposedBridge.log("Preventing "+ methodName +" operation!!!");
        findAndHookMethod(classToHook, lpparam.classLoader, functionToHook, "org.wso2.emm.agent.beans.Operation", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                XposedBridge.log("The Method: "+methodName+" prevented.");
                return null;
            }
        } );
    }

    /* find and hook before method called */
    public void detectAndHookBefore(final LoadPackageParam loadPackageParam, String methodName){

        final String classToHook = loadPackageParam.packageName+".services.MessageProcessor";
        final String functionToHook = methodName;
        findAndHookMethod(classToHook, loadPackageParam.classLoader, functionToHook, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
                XposedBridge.log("find and hooked on "+loadPackageParam.packageName+" "+classToHook+" "+functionToHook);
            }
        });
    }


    /* for location */
    public void changeLocation(LoadPackageParam loadPackageParam){

       final String classToHook = loadPackageParam.packageName+".api.GPSTracker";
       final String functionToHook1 = "getLatitude";
       final String functionToHook2 = "getLongitude";

        XposedBridge.log("Changing "+ functionToHook1 +" operation!!!");
        XposedBridge.log("Changing "+ functionToHook2 +" operation!!!");

        findAndHookMethod(classToHook, loadPackageParam.classLoader, functionToHook1, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                double lat = (double) param.thisObject;
                XposedBridge.log("in a "+ functionToHook1 +" operation!!!: "+ String.valueOf(lat));
            }
        });
        findAndHookMethod(classToHook, loadPackageParam.classLoader, functionToHook2, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                double lon = (double) param.thisObject;
                XposedBridge.log("in a "+ functionToHook2 +" operation: " + String.valueOf(lon));
            }
        });

    }


}

