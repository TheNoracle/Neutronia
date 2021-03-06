package team.abnormal.neutronia.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference {

    public static final String MOD_ID = "neutronia";
    public static final String MOD_NAME = "Neutronia";
    public static final String VERSION = "0.0.2";
    public static final String DEPENDENCIES = "required-before:abnormalib@[0.0.1,);";
    public static final String PREFIX_MOD = MOD_ID + ":";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final String COMMON_PROXY = "team.abnormal.neutronia.base.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "team.abnormal.neutronia.base.proxy.ClientProxy";
    public static final String GUI_FACTORY = "team.abnormal.neutronia.base.config.NGuiFactory";

}
