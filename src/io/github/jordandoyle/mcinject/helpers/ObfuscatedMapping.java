package io.github.jordandoyle.mcinject.helpers;

import static org.apache.commons.lang3.StringUtils.*;

import javax.swing.JOptionPane;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ObfuscatedMapping {
	private String owner, name, desc, obfOwner, obfName;

	private static boolean obfuscated = false;

	static {
		try {
			Class test = ObfuscatedMapping.class.getClassLoader().loadClass(
					"net.minecraft.src.Block");
		} catch (Exception e) {
			obfuscated = true;
		}
	}

	public ObfuscatedMapping(String owner, String name, String desc) {
		this.owner = owner;
		this.name = name;
		this.desc = desc;

		if (!isClass()) {
			this.obfName = getMapping();
			this.obfOwner = new ObfuscatedMapping(owner).getMapping();
		}

		if (contains(owner, "."))
			throw new IllegalArgumentException(owner);
	}

	public ObfuscatedMapping(String owner) {
		this(owner, "", "");
	}

	public ObfuscatedMapping(String owner, String name) {
		this(owner, name, "");
	}

	public boolean equals(MethodInsnNode node) {
		return (obfName.equals(node.name) || name.equals(node.name))
				&& desc.equals(node.desc)
				&& (obfOwner.equals(node.owner) || owner.equals(node.owner));
	}

	public boolean equals(FieldInsnNode node) {
		return (obfName.equals(node.name) || name.equals(node.name))
				&& (obfOwner.equals(node.owner) || owner.equals(node.owner));
	}

	public boolean equals(MethodNode node) {
		return (obfName.equals(node.name) || name.equals(node.name))
				&& desc.equals(node.desc);
	}

	public boolean equals(FieldNode node) {
		return (obfName.equals(node.name) || name.equals(node.name));
	}

	public String getRealOwner() {
		return replace(owner, "/", ".");
	}

	@Override
	public String toString() {
		return getMapping();
	}

	public boolean isClass() {
		return name.length() == 0;
	}

	public boolean isMethod() {
		return desc.contains("(");
	}

	public boolean isField() {
		return !isClass() && !isMethod();
	}

	public String getMapping() {
		if (!obfuscated)
			return (isClass()) ? getRealOwner() : name;

		if (isClass())
			return Mapper.getClass(getRealOwner());

		if (isMethod())
			return Mapper.getMethod(Mapper.getClass(getRealOwner()), name);

		if (isField())
			return Mapper.getField(Mapper.getClass(getRealOwner()), name);

		return getRealOwner();
	}
}
