package ru.nsu.fit.g13205.zharkova.view;

import ru.nsu.fit.g13205.zharkova.model.DefaultSetting;
import java.io.File;
/**
 * Created by Yana on 22.02.16.
 */
public class GridSettings {

    private int hexSize;
    private int lineSize;

    private boolean impact = false;
    private boolean replace = true;

    private File currentFile;
    private boolean isChanges;

    public GridSettings(){
        this.currentFile = null;
        this.isChanges = false;
        this.hexSize = DefaultSetting.DEFAULT_HEX_SIZE;
        this.lineSize = DefaultSetting.DEFAULT_HEX_WIDTH;
        this.impact = DefaultSetting.DEFAULT_IMPACT_MODE;
        this.replace = DefaultSetting.DEFAULT_MODE;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    public int getHexSize() {
        return hexSize;
    }

    public void setHexSize(int hexSize) {
        this.hexSize = hexSize;
    }

    public boolean isImpact() {
        return impact;
    }

    public void setImpact(boolean impact) {
        this.impact = impact;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public boolean isChanges() {
        return isChanges;
    }

    public void setChanges(boolean isChanges) {
        this.isChanges = isChanges;
    }

    public void setDefault() {
        this.hexSize = DefaultSetting.DEFAULT_HEX_SIZE;
        this.lineSize = DefaultSetting.DEFAULT_HEX_WIDTH;
        this.impact = DefaultSetting.DEFAULT_IMPACT_MODE;
        this.replace = DefaultSetting.DEFAULT_MODE;
    }
}
