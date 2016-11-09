package memphis.edu.deviceinspectorforemm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sujit on 10/31/2016.
 */
public class AppInfo {
    private String name;
    private String packageName;
    private String version;
    private List<String> reqPermissions = new ArrayList<String>();

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPackageName(){
        return packageName;
    }
    public void setPackageName(String packageName){
        this.packageName = packageName;
    }
    public void setVersion(String version){
        this.version = version;
    }
    public String getVersion(){
        return version;
    }
    public List<String> getReqPermissions(){
        return reqPermissions;
    }
    public void setReqPermissions(String reqPermissions){
        this.reqPermissions.add(reqPermissions);
    }

}
