package io.github.jordandoyle.mcinject.asm.transformers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import io.github.jordandoyle.mcinject.asm.ClassTransformer;
import io.github.jordandoyle.mcinject.helpers.ObfuscatedMapping;

public class EntityPlayerTransformer extends ClassTransformer {

	@Override
	public void transform() {
		ObfuscatedMapping cl = new ObfuscatedMapping("net/minecraft/src/EntityPlayer");

		InputStream in = getClass().getResourceAsStream("/" + cl.toString().replace(".", "/") + ".class");
		
		// mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/src/EntityPlayer", "applyArmorCalculations", "(Lnet/minecraft/src/DamageSource;F)F");
		
		try {
			ClassReader cr = new ClassReader(in);
			ClassNode cn = new ClassNode();
			cr.accept(cn, 0);
			
			for (MethodNode mn : cn.methods) {
				if (!mn.name.equals(new ObfuscatedMapping("net/minecraft/src/EntityLivingBase", "damageEntity", "(").getMapping()))
					continue;

				Iterator<AbstractInsnNode> insnNodes = mn.instructions
						.iterator();

				while (insnNodes.hasNext()) {
					AbstractInsnNode insn = insnNodes.next();

					if (insn.getType() == insn.METHOD_INSN) {
						MethodInsnNode node = (MethodInsnNode) insn;
						
						// TODO: Fix ObfuscatedMapping to work with obfuscated parameter types
						if (node.name.equals(new ObfuscatedMapping("net/minecraft/src/EntityLivingBase", "applyArmorCalculations", "(").getMapping())) {
							InsnList list = new InsnList();
							
							list.add(new VarInsnNode(Opcodes.ALOAD, 1));
							list.add(new VarInsnNode(Opcodes.FLOAD, 2));
							list.add(new MethodInsnNode(
									Opcodes.INVOKESTATIC,
                                    "io/github/jordandoyle/mcinject/event/EventFactory",
									"damageEntity", "(Ljava/lang/Object;F)V"));
							
							mn.instructions.insert(insn, list);
						}
					}
				}
			}

			ClassWriter cw = new ClassWriter(0);
			cn.accept(cw);
			
			loadClass(cw.toByteArray(), new ObfuscatedMapping("net/minecraft/src/EntityPlayer").getMapping());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
