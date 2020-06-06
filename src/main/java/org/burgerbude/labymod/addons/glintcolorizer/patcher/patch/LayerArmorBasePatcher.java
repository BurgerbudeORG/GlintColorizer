package org.burgerbude.labymod.addons.glintcolorizer.patcher.patch;

import net.labymod.core.asm.LabyModCoreMod;
import org.burgerbude.labymod.addons.glintcolorizer.GlintColorizerAddon;
import org.burgerbude.labymod.addons.glintcolorizer.GlintColorizerTransformer;
import org.burgerbude.labymod.addons.glintcolorizer.patcher.Patcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * A patcher which patched the <b>LayerArmorBase</b> class
 *
 * @author Robby
 */
public class LayerArmorBasePatcher implements Patcher {

    private final String renderEnchantGlintDesc = (LabyModCoreMod.isForge() ? (GlintColorizerTransformer.OLD_MC ?
            "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/model/ModelBase;FFFFFFF)V" :
            "(Lnet/minecraft/client/renderer/entity/RenderLivingBase;Lnet/minecraft/entity/EntityLivingBase;" +
                    "Lnet/minecraft/client/model/ModelBase;FFFFFFF)V") :
            (GlintColorizerTransformer.OLD_MC ? "(Lpr;Lbbo;FFFFFFF)V" :
                    "(Lcaa;Lvp;Lbqf;FFFFFFF)V"));


    @Override
    public void patch(ClassNode classNode) {
        boolean flag = false;

        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.desc.equals(this.renderEnchantGlintDesc)) {
                if (GlintColorizerTransformer.OLD_MC) {

                    for (AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
                        if (abstractInsnNode.getOpcode() == Opcodes.LDC) {
                            Object object = ((LdcInsnNode) abstractInsnNode).cst;
                            if (object.equals(0.5F) && !flag) {
                                flag = true;
                            } else if (object.equals(0.5F) && flag) {
                                methodNode.instructions.insert(abstractInsnNode, colors(0));
                                methodNode.instructions.remove(abstractInsnNode);
                            }

                            if (object.equals(0.25F)) {
                                methodNode.instructions.insert(abstractInsnNode, colors(1));
                                methodNode.instructions.remove(abstractInsnNode);
                            }

                            if (object.equals(0.8F)) {
                                methodNode.instructions.insert(abstractInsnNode, colors(2));
                                methodNode.instructions.remove(abstractInsnNode);
                            }
                        }
                    }
                } else {

                    for (AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
                        if (abstractInsnNode.getOpcode() == Opcodes.LDC) {

                            Object object = ((LdcInsnNode) abstractInsnNode).cst;
                            if (object.equals(0.38F)) {
                                methodNode.instructions.insert(abstractInsnNode, colors(0));
                                methodNode.instructions.remove(abstractInsnNode);
                            }
                            if (object.equals(0.19F)) {
                                methodNode.instructions.insert(abstractInsnNode, colors(1));
                                methodNode.instructions.remove(abstractInsnNode);
                            }
                            if (object.equals(0.608F)) {
                                methodNode.instructions.insert(abstractInsnNode, colors(2));
                                methodNode.instructions.remove(abstractInsnNode);
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * Creates a doubly linked list of {@link AbstractInsnNode} objects.<br>
     * This list contains contains the {@link GlintColorizerAddon#hexadecimalToRGBA()}
     *
     * @param index The color index
     * @return a doubly linked list of {@link AbstractInsnNode} objects.
     */
    public InsnList colors(int index) {
        InsnList insnList = new InsnList();
        insnList.insert(new InsnNode(Opcodes.FALOAD));
        if (index == 0) {
            insnList.insert(new InsnNode(Opcodes.ICONST_0));
        } else if (index == 1) {
            insnList.insert(new InsnNode(Opcodes.ICONST_1));
        } else if (index == 2) {
            insnList.insert(new InsnNode(Opcodes.ICONST_2));
        } else {
            insnList.insert(new InsnNode(Opcodes.ICONST_0));
        }
        insnList.insert(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                "org/burgerbude/labymod/addons/glintcolorizer/GlintColorizerAddon",
                "hexadecimalToRGBA",
                "()[F",
                false));
        insnList.insert(new FieldInsnNode(Opcodes.GETSTATIC,
                "org/burgerbude/labymod/addons/glintcolorizer/GlintColorizerAddon",
                "instance",
                "Lorg/burgerbude/labymod/addons/glintcolorizer/GlintColorizerAddon;"));
        return insnList;
    }

}
