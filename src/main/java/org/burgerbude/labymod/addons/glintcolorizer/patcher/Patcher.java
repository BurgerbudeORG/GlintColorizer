package org.burgerbude.labymod.addons.glintcolorizer.patcher;

import org.objectweb.asm.tree.ClassNode;

/**
 * Represents a patcher which allowed to patches classes
 *
 * @author Robby
 */
public interface Patcher {

    /**
     * The logic for the patch progress
     *
     * @param classNode Node of the class which should be patch
     */
    void patch(ClassNode classNode);

}
