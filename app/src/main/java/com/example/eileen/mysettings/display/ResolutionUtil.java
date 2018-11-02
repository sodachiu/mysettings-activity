package com.example.eileen.mysettings.display;

import android.content.Context;
import android.os.display.DisplayManager;

import com.example.eileen.mysettings.Resolution;

import java.util.ArrayList;
import java.util.List;

public class ResolutionUtil {

    private static List<Resolution> mResolutionList;
    private static final int DISPLAY_ADAPTIVE = 111222;
    public static List<Resolution> initResolutionList(DisplayManager displayManager){
        mResolutionList = new ArrayList<>();
        int[] allSupportStandards = displayManager.getAllSupportStandards();
        int currentStandard = displayManager.getCurrentStandard();
        Resolution displayAdaptive = new Resolution("自适应",
                DISPLAY_ADAPTIVE);
        mResolutionList.add(displayAdaptive);

        for (int item : allSupportStandards) {
            switch (item){
                case DisplayManager.DISPLAY_STANDARD_1080P_60:

                    Resolution display1080P60Hz = new Resolution("1080P 60Hz",
                            DisplayManager.DISPLAY_STANDARD_1080P_60);
                    if (DisplayManager.DISPLAY_STANDARD_1080P_60 == currentStandard){
                        display1080P60Hz.setIschecked(true);
                    }
                    mResolutionList.add(display1080P60Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080P_50:
                    Resolution display1080P50Hz = new Resolution("1080P 50Hz",
                            DisplayManager.DISPLAY_STANDARD_1080P_50);
                    if (DisplayManager.DISPLAY_STANDARD_1080P_50 == currentStandard){
                        display1080P50Hz.setIschecked(true);
                    }
                    mResolutionList.add(display1080P50Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080P_30:
                    Resolution display1080P30Hz = new Resolution("1080P 30Hz",
                            DisplayManager.DISPLAY_STANDARD_1080P_30);
                    if (DisplayManager.DISPLAY_STANDARD_1080P_30 == currentStandard){
                        display1080P30Hz.setIschecked(true);
                    }
                    mResolutionList.add(display1080P30Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080P_25:
                    Resolution display1080P25Hz = new Resolution("1080P 25Hz",
                            DisplayManager.DISPLAY_STANDARD_1080P_25);
                    if (DisplayManager.DISPLAY_STANDARD_1080P_25 == currentStandard){
                        display1080P25Hz.setIschecked(true);
                    }
                    mResolutionList.add(display1080P25Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080P_24:
                    Resolution display1080P24Hz = new Resolution("1080P 24Hz",
                            DisplayManager.DISPLAY_STANDARD_1080P_24);
                    if (DisplayManager.DISPLAY_STANDARD_1080P_24 == currentStandard)
                        mResolutionList.add(display1080P24Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080I_60:
                    Resolution display1080i60Hz = new Resolution("1080i 60Hz",
                            DisplayManager.DISPLAY_STANDARD_1080I_60);
                    if (DisplayManager.DISPLAY_STANDARD_1080I_60 == currentStandard){
                        display1080i60Hz.setIschecked(true);
                    }
                    mResolutionList.add(display1080i60Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_1080I_50:
                    Resolution display1080i50Hz = new Resolution("1080i 50Hz",
                            DisplayManager.DISPLAY_STANDARD_1080I_50);
                    if (DisplayManager.DISPLAY_STANDARD_1080I_50 == currentStandard){
                        display1080i50Hz.setIschecked(true);
                    }
                    mResolutionList.add(display1080i50Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_720P_60:
                    Resolution display720p60Hz = new Resolution("720P 60Hz",
                            DisplayManager.DISPLAY_STANDARD_720P_60);
                    if (DisplayManager.DISPLAY_STANDARD_720P_60 == currentStandard){
                        display720p60Hz.setIschecked(true);
                    }
                    mResolutionList.add(display720p60Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_720P_50:
                    Resolution display720p50Hz = new Resolution("720P 50Hz",
                            DisplayManager.DISPLAY_STANDARD_720P_50);
                    if (DisplayManager.DISPLAY_STANDARD_720P_50 == currentStandard){
                        display720p50Hz.setIschecked(true);
                    }
                    mResolutionList.add(display720p50Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_480P_60:
                    Resolution display480p60Hz = new Resolution("480P 60Hz",
                            DisplayManager.DISPLAY_STANDARD_480P_60);
                    if (DisplayManager.DISPLAY_STANDARD_480P_60 == currentStandard){
                        display480p60Hz.setIschecked(true);
                    }
                    mResolutionList.add(display480p60Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_PAL:
                    Resolution displayPal = new Resolution("PAL",
                            DisplayManager.DISPLAY_STANDARD_PAL);
                    if (DisplayManager.DISPLAY_STANDARD_PAL == currentStandard){
                        displayPal.setIschecked(true);
                    }
                    mResolutionList.add(displayPal);
                    break;
                case DisplayManager.DISPLAY_STANDARD_NTSC:
                    Resolution displayNtsc = new Resolution("NTSL",
                            DisplayManager.DISPLAY_STANDARD_NTSC);
                    if (DisplayManager.DISPLAY_STANDARD_NTSC == currentStandard){
                        displayNtsc.setIschecked(true);
                    }
                    mResolutionList.add(displayNtsc);
                    break;
                case DisplayManager.DISPLAY_STANDARD_3840_2160P_24:
                    Resolution display3840x2160p24Hz = new Resolution("3840 x 2160P 24Hz",
                            DisplayManager.DISPLAY_STANDARD_3840_2160P_24);
                    if (DisplayManager.DISPLAY_STANDARD_3840_2160P_24 == currentStandard){
                        display3840x2160p24Hz.setIschecked(true);
                    }
                    mResolutionList.add(display3840x2160p24Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_3840_2160P_25:
                    Resolution display3840x2160p25Hz = new Resolution("3840 x 2160P 25Hz",
                            DisplayManager.DISPLAY_STANDARD_3840_2160P_25);
                    if (DisplayManager.DISPLAY_STANDARD_3840_2160P_25 == currentStandard){
                        display3840x2160p25Hz.setIschecked(true);
                    }
                    mResolutionList.add(display3840x2160p25Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_3840_2160P_30:
                    Resolution display3840x2160p30Hz = new Resolution("3840 x 2160P 30Hz",
                            DisplayManager.DISPLAY_STANDARD_3840_2160P_30);
                    if (DisplayManager.DISPLAY_STANDARD_3840_2160P_30 == currentStandard){
                        display3840x2160p30Hz.setIschecked(true);
                    }
                    mResolutionList.add(display3840x2160p30Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_3840_2160P_60:
                    Resolution display3840x2160p60Hz = new Resolution("3840 x 2160P 60Hz",
                            DisplayManager.DISPLAY_STANDARD_3840_2160P_60);
                    if (DisplayManager.DISPLAY_STANDARD_3840_2160P_60 == currentStandard){
                        display3840x2160p60Hz.setIschecked(true);
                    }
                    mResolutionList.add(display3840x2160p60Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_4096_2160P_24:
                    Resolution display4096x2160p24Hz = new Resolution("4096 x 2160P 60Hz",
                            DisplayManager.DISPLAY_STANDARD_4096_2160P_24);
                    if (DisplayManager.DISPLAY_STANDARD_4096_2160P_24 == currentStandard){
                        display4096x2160p24Hz.setIschecked(true);
                    }
                    mResolutionList.add(display4096x2160p24Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_4096_2160P_25:
                    Resolution display4096x2160p25Hz = new Resolution("4096 x 2160P 25Hz",
                            DisplayManager.DISPLAY_STANDARD_4096_2160P_25);
                    if (display4096x2160p25Hz.getId() == currentStandard){
                        display4096x2160p25Hz.setIschecked(true);
                    }
                    mResolutionList.add(display4096x2160p25Hz);
                    break;
                case DisplayManager. DISPLAY_STANDARD_4096_2160P_30:
                    Resolution display4096x2160p30Hz = new Resolution("4096 x 2160P 30Hz",
                            DisplayManager. DISPLAY_STANDARD_4096_2160P_30);
                    if (display4096x2160p30Hz.getId() == currentStandard){
                        display4096x2160p30Hz.setIschecked(true);
                    }
                    mResolutionList.add(display4096x2160p30Hz);
                    break;
                case DisplayManager.DISPLAY_STANDARD_4096_2160P_60:
                    Resolution display4096x2160p60Hz = new Resolution("4096 x 2160P 60Hz",
                            DisplayManager.DISPLAY_STANDARD_4096_2160P_60);
                    if (display4096x2160p60Hz.getId() == currentStandard){
                        display4096x2160p60Hz.setIschecked(true);
                    }
                    mResolutionList.add(display4096x2160p60Hz);
                    break;
                default:
                    break;
            }

        }


        return mResolutionList;
    }
}
