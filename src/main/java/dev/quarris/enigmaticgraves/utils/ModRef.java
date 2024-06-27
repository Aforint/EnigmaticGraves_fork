package dev.quarris.enigmaticgraves.utils;

import dev.quarris.enigmaticgraves.EnigmaticGraves;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModRef {

    public static final String ID = "enigmaticgraves";
    public static final Logger LOGGER = LogManager.getLogger(EnigmaticGraves.class);

    public static ResourceLocation res(String res) {
        return ResourceLocation.fromNamespaceAndPath(ID, res);
    }

}
