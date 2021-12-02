package io.github.jordandoyle.mcinject.asm;

import java.util.ArrayList;
import java.util.List;

import io.github.jordandoyle.mcinject.asm.transformers.*;

public class TransformerManager {
	private static List<ClassTransformer> transformers = new ArrayList<ClassTransformer>();
	
	static {
		transformers.add(new InGameTransformer());
		transformers.add(new MinecraftTransformer());
		transformers.add(new EntityRendererTransformer());
		transformers.add(new EntityPlayerTransformer());
	}
	
	public static void runTransformers() {
		for(ClassTransformer transformer : transformers)
			transformer.transform();
	}
}
