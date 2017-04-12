package com.koceeng.freedonation.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.koceeng.freedonation.util.PermissionUtil;
import com.koceeng.freedonation.util.PermissionUtil.PermissionCallbacks;

import java.util.Arrays;

public class PermissionHelper {

    private PermissionCallbacks object;
    private Context context;
    private Activity thisActivity;
    private Fragment thisFragment;
    private int requestCodePermission;
    private int requestCodeSetting;
    private String[] perms;

    public PermissionHelper(Object object, Context context) {
        this.context = context;
        if (object instanceof PermissionCallbacks)
            this.object = (PermissionCallbacks) object;

        if (object instanceof Activity) {
            this.thisActivity = (Activity) object;

        } else if (object instanceof Fragment) {
            this.thisFragment = (Fragment) object;
        }
    }

    public PermissionHelper setRequestCodePermission(int requestCodePermission) {
        this.requestCodePermission = requestCodePermission;
        return this;
    }

    public PermissionHelper setRequestCodeSetting(int requestCodeSetting) {
        this.requestCodeSetting = requestCodeSetting;
        return this;
    }

    public PermissionHelper setPermissions(String[] perms) {
        this.perms = perms;
        return this;
    }

    public PermissionHelper setPermission(String perm) {
        this.perms = new String[]{perm};
        return this;
    }

    public void init() {
        init(false);
    }

    private void init(boolean fromSetting) {
        if (PermissionUtil.hasPermissions(context, perms)) {
            object.onPermissionsGranted(requestCodePermission, Arrays.asList(perms));
        } else {
            if (!fromSetting) {
                PermissionUtil.requestPermissions(object, requestCodePermission, perms);
            } else {
                // if from setting and still not granted
                object.onPermissionsDenied(requestCodePermission, Arrays.asList(perms));
            }
        }
    }

    public void onRequestPermissionsResult(@NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCodePermission, perms, grantResults, object);
    }

    public void onRationaleOk(Boolean isPermanentDeny) {
        if (!isPermanentDeny) {
            PermissionUtil.executePermissionsRequest(object, perms, requestCodePermission);
        } else {
            // Create app settings intent
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);

            // Start for result
            if (object instanceof Activity) {
                ((Activity) object).startActivityForResult(intent, requestCodeSetting);
            } else if (object instanceof Fragment) {
                ((Fragment) object).startActivityForResult(intent, requestCodeSetting);
            } else if (object instanceof android.app.Fragment) {
                ((android.app.Fragment) object).startActivityForResult(intent, requestCodeSetting);
            }
        }
    }

    public void denied() {
        if (PermissionUtil.somePermissionPermanentlyDenied(object, Arrays.asList(perms))) {
            object.onShowPermissionPermanentDeny();
        } else {
            object.onShowPermissionRationale();
        }
    }

    public void fromSetting() {
        init(true);
    }
}
