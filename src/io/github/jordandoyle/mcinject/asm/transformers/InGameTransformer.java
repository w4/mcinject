package io.github.jordandoyle.mcinject.asm.transformers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import io.github.jordandoyle.mcinject.asm.*;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;

public class InGameTransformer extends ClassTransformer {

	public void transform() {
		ObfuscatedMapping cl            = new ObfuscatedMapping("net/minecraft/src/GuiIngame");
		ObfuscatedMapping renderOverlay = new ObfuscatedMapping("net/minecraft/src/GuiIngame", "renderGameOverlay", Type.getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.BOOLEAN_TYPE, Type.INT_TYPE, Type.INT_TYPE));
		ObfuscatedMapping gameSettings  = new ObfuscatedMapping("net/minecraft/src/GameSettings", "showDebugInfo");
		
		InputStream in = getClass().getResourceAsStream("/" + cl.toString().replace(".", "/") + ".class");
		
		try {
			ClassReader cr = new ClassReader(in);
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);
			
			for (MethodNode mn : cn.methods) {
				if (!renderOverlay.equals(mn))
					continue;
				
				Iterator<AbstractInsnNode> insnNodes = mn.instructions
						.iterator();

				while (insnNodes.hasNext()) {
					AbstractInsnNode insn = insnNodes.next();
					
					if (insn.getType() == insn.FIELD_INSN) {
						FieldInsnNode node = (FieldInsnNode) insn;
						
						if (gameSettings.equals(node)) {
							InsnList list = new InsnList();
							
							list.add(new MethodInsnNode(
									Opcodes.INVOKESTATIC,
                                    "io/github/jordandoyle/mcinject/event/EventFactory",
									"renderOverlay", "()V"));
							
							mn.instructions.insertBefore(insn, list);
						}
					}
				}
				break;
			}

			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);
			
			loadClass(cw.toByteArray(), new ObfuscatedMapping("net/minecraft/src/GuiIngame").getMapping());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}