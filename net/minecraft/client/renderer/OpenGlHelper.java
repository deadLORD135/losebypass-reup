/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.ARBFramebufferObject
 *  org.lwjgl.opengl.ARBMultitexture
 *  org.lwjgl.opengl.ARBShaderObjects
 *  org.lwjgl.opengl.ARBVertexBufferObject
 *  org.lwjgl.opengl.ARBVertexShader
 *  org.lwjgl.opengl.ContextCapabilities
 *  org.lwjgl.opengl.EXTBlendFuncSeparate
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL14
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.opengl.GLContext
 *  oshi.SystemInfo
 *  oshi.hardware.Processor
 */
package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTBlendFuncSeparate;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import oshi.SystemInfo;
import oshi.hardware.Processor;

public class OpenGlHelper {
    public static boolean nvidia;
    public static boolean field_181063_b;
    public static int GL_FRAMEBUFFER;
    public static int GL_RENDERBUFFER;
    public static int GL_COLOR_ATTACHMENT0;
    public static int GL_DEPTH_ATTACHMENT;
    public static int GL_FRAMEBUFFER_COMPLETE;
    public static int GL_FB_INCOMPLETE_ATTACHMENT;
    public static int GL_FB_INCOMPLETE_MISS_ATTACH;
    public static int GL_FB_INCOMPLETE_DRAW_BUFFER;
    public static int GL_FB_INCOMPLETE_READ_BUFFER;
    private static int framebufferType;
    public static boolean framebufferSupported;
    private static boolean shadersAvailable;
    private static boolean arbShaders;
    public static int GL_LINK_STATUS;
    public static int GL_COMPILE_STATUS;
    public static int GL_VERTEX_SHADER;
    public static int GL_FRAGMENT_SHADER;
    private static boolean arbMultitexture;
    public static int defaultTexUnit;
    public static int lightmapTexUnit;
    public static int GL_TEXTURE2;
    private static boolean arbTextureEnvCombine;
    public static int GL_COMBINE;
    public static int GL_INTERPOLATE;
    public static int GL_PRIMARY_COLOR;
    public static int GL_CONSTANT;
    public static int GL_PREVIOUS;
    public static int GL_COMBINE_RGB;
    public static int GL_SOURCE0_RGB;
    public static int GL_SOURCE1_RGB;
    public static int GL_SOURCE2_RGB;
    public static int GL_OPERAND0_RGB;
    public static int GL_OPERAND1_RGB;
    public static int GL_OPERAND2_RGB;
    public static int GL_COMBINE_ALPHA;
    public static int GL_SOURCE0_ALPHA;
    public static int GL_SOURCE1_ALPHA;
    public static int GL_SOURCE2_ALPHA;
    public static int GL_OPERAND0_ALPHA;
    public static int GL_OPERAND1_ALPHA;
    public static int GL_OPERAND2_ALPHA;
    private static boolean openGL14;
    public static boolean extBlendFuncSeparate;
    public static boolean openGL21;
    public static boolean shadersSupported;
    private static String logText;
    private static String field_183030_aa;
    public static boolean vboSupported;
    public static boolean field_181062_Q;
    private static boolean arbVbo;
    public static int GL_ARRAY_BUFFER;
    public static int GL_STATIC_DRAW;

    public static void initializeTextures() {
        ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        arbMultitexture = contextcapabilities.GL_ARB_multitexture && !contextcapabilities.OpenGL13;
        boolean bl = arbTextureEnvCombine = contextcapabilities.GL_ARB_texture_env_combine && !contextcapabilities.OpenGL13;
        if (arbMultitexture) {
            logText = logText + "Using ARB_multitexture.\n";
            defaultTexUnit = 33984;
            lightmapTexUnit = 33985;
            GL_TEXTURE2 = 33986;
        } else {
            logText = logText + "Using GL 1.3 multitexturing.\n";
            defaultTexUnit = 33984;
            lightmapTexUnit = 33985;
            GL_TEXTURE2 = 33986;
        }
        if (arbTextureEnvCombine) {
            logText = logText + "Using ARB_texture_env_combine.\n";
            GL_COMBINE = 34160;
            GL_INTERPOLATE = 34165;
            GL_PRIMARY_COLOR = 34167;
            GL_CONSTANT = 34166;
            GL_PREVIOUS = 34168;
            GL_COMBINE_RGB = 34161;
            GL_SOURCE0_RGB = 34176;
            GL_SOURCE1_RGB = 34177;
            GL_SOURCE2_RGB = 34178;
            GL_OPERAND0_RGB = 34192;
            GL_OPERAND1_RGB = 34193;
            GL_OPERAND2_RGB = 34194;
            GL_COMBINE_ALPHA = 34162;
            GL_SOURCE0_ALPHA = 34184;
            GL_SOURCE1_ALPHA = 34185;
            GL_SOURCE2_ALPHA = 34186;
            GL_OPERAND0_ALPHA = 34200;
            GL_OPERAND1_ALPHA = 34201;
            GL_OPERAND2_ALPHA = 34202;
        } else {
            logText = logText + "Using GL 1.3 texture combiners.\n";
            GL_COMBINE = 34160;
            GL_INTERPOLATE = 34165;
            GL_PRIMARY_COLOR = 34167;
            GL_CONSTANT = 34166;
            GL_PREVIOUS = 34168;
            GL_COMBINE_RGB = 34161;
            GL_SOURCE0_RGB = 34176;
            GL_SOURCE1_RGB = 34177;
            GL_SOURCE2_RGB = 34178;
            GL_OPERAND0_RGB = 34192;
            GL_OPERAND1_RGB = 34193;
            GL_OPERAND2_RGB = 34194;
            GL_COMBINE_ALPHA = 34162;
            GL_SOURCE0_ALPHA = 34184;
            GL_SOURCE1_ALPHA = 34185;
            GL_SOURCE2_ALPHA = 34186;
            GL_OPERAND0_ALPHA = 34200;
            GL_OPERAND1_ALPHA = 34201;
            GL_OPERAND2_ALPHA = 34202;
        }
        extBlendFuncSeparate = contextcapabilities.GL_EXT_blend_func_separate && !contextcapabilities.OpenGL14;
        openGL14 = contextcapabilities.OpenGL14 || contextcapabilities.GL_EXT_blend_func_separate;
        boolean bl2 = framebufferSupported = openGL14 && (contextcapabilities.GL_ARB_framebuffer_object || contextcapabilities.GL_EXT_framebuffer_object || contextcapabilities.OpenGL30);
        if (framebufferSupported) {
            logText = logText + "Using framebuffer objects because ";
            if (contextcapabilities.OpenGL30) {
                logText = logText + "OpenGL 3.0 is supported and separate blending is supported.\n";
                framebufferType = 0;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            } else if (contextcapabilities.GL_ARB_framebuffer_object) {
                logText = logText + "ARB_framebuffer_object is supported and separate blending is supported.\n";
                framebufferType = 1;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            } else if (contextcapabilities.GL_EXT_framebuffer_object) {
                logText = logText + "EXT_framebuffer_object is supported.\n";
                framebufferType = 2;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
        } else {
            logText = logText + "Not using framebuffer objects because ";
            logText = logText + "OpenGL 1.4 is " + (contextcapabilities.OpenGL14 ? "" : "not ") + "supported, ";
            logText = logText + "EXT_blend_func_separate is " + (contextcapabilities.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
            logText = logText + "OpenGL 3.0 is " + (contextcapabilities.OpenGL30 ? "" : "not ") + "supported, ";
            logText = logText + "ARB_framebuffer_object is " + (contextcapabilities.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
            logText = logText + "EXT_framebuffer_object is " + (contextcapabilities.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
        }
        openGL21 = contextcapabilities.OpenGL21;
        shadersAvailable = openGL21 || contextcapabilities.GL_ARB_vertex_shader && contextcapabilities.GL_ARB_fragment_shader && contextcapabilities.GL_ARB_shader_objects;
        logText = logText + "Shaders are " + (shadersAvailable ? "" : "not ") + "available because ";
        if (shadersAvailable) {
            if (contextcapabilities.OpenGL21) {
                logText = logText + "OpenGL 2.1 is supported.\n";
                arbShaders = false;
                GL_LINK_STATUS = 35714;
                GL_COMPILE_STATUS = 35713;
                GL_VERTEX_SHADER = 35633;
                GL_FRAGMENT_SHADER = 35632;
            } else {
                logText = logText + "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
                arbShaders = true;
                GL_LINK_STATUS = 35714;
                GL_COMPILE_STATUS = 35713;
                GL_VERTEX_SHADER = 35633;
                GL_FRAGMENT_SHADER = 35632;
            }
        } else {
            logText = logText + "OpenGL 2.1 is " + (contextcapabilities.OpenGL21 ? "" : "not ") + "supported, ";
            logText = logText + "ARB_shader_objects is " + (contextcapabilities.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
            logText = logText + "ARB_vertex_shader is " + (contextcapabilities.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
            logText = logText + "ARB_fragment_shader is " + (contextcapabilities.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
        }
        shadersSupported = framebufferSupported && shadersAvailable;
        String s = GL11.glGetString((int)7936).toLowerCase();
        nvidia = s.contains("nvidia");
        arbVbo = !contextcapabilities.OpenGL15 && contextcapabilities.GL_ARB_vertex_buffer_object;
        vboSupported = contextcapabilities.OpenGL15 || arbVbo;
        logText = logText + "VBOs are " + (vboSupported ? "" : "not ") + "available because ";
        if (vboSupported) {
            if (arbVbo) {
                logText = logText + "ARB_vertex_buffer_object is supported.\n";
                GL_STATIC_DRAW = 35044;
                GL_ARRAY_BUFFER = 34962;
            } else {
                logText = logText + "OpenGL 1.5 is supported.\n";
                GL_STATIC_DRAW = 35044;
                GL_ARRAY_BUFFER = 34962;
            }
        }
        if (field_181063_b = s.contains("ati")) {
            if (vboSupported) {
                field_181062_Q = true;
            } else {
                GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0f);
            }
        }
        try {
            Processor[] aprocessor = new SystemInfo().getHardware().getProcessors();
            field_183030_aa = String.format("%dx %s", aprocessor.length, aprocessor[0]).replaceAll("\\s+", " ");
            return;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static boolean areShadersSupported() {
        return shadersSupported;
    }

    public static String getLogText() {
        return logText;
    }

    public static int glGetProgrami(int program, int pname) {
        int n;
        if (arbShaders) {
            n = ARBShaderObjects.glGetObjectParameteriARB((int)program, (int)pname);
            return n;
        }
        n = GL20.glGetProgrami((int)program, (int)pname);
        return n;
    }

    public static void glAttachShader(int program, int shaderIn) {
        if (arbShaders) {
            ARBShaderObjects.glAttachObjectARB((int)program, (int)shaderIn);
            return;
        }
        GL20.glAttachShader((int)program, (int)shaderIn);
    }

    public static void glDeleteShader(int p_153180_0_) {
        if (arbShaders) {
            ARBShaderObjects.glDeleteObjectARB((int)p_153180_0_);
            return;
        }
        GL20.glDeleteShader((int)p_153180_0_);
    }

    public static int glCreateShader(int type) {
        int n;
        if (arbShaders) {
            n = ARBShaderObjects.glCreateShaderObjectARB((int)type);
            return n;
        }
        n = GL20.glCreateShader((int)type);
        return n;
    }

    public static void glShaderSource(int shaderIn, ByteBuffer string) {
        if (arbShaders) {
            ARBShaderObjects.glShaderSourceARB((int)shaderIn, (ByteBuffer)string);
            return;
        }
        GL20.glShaderSource((int)shaderIn, (ByteBuffer)string);
    }

    public static void glCompileShader(int shaderIn) {
        if (arbShaders) {
            ARBShaderObjects.glCompileShaderARB((int)shaderIn);
            return;
        }
        GL20.glCompileShader((int)shaderIn);
    }

    public static int glGetShaderi(int shaderIn, int pname) {
        int n;
        if (arbShaders) {
            n = ARBShaderObjects.glGetObjectParameteriARB((int)shaderIn, (int)pname);
            return n;
        }
        n = GL20.glGetShaderi((int)shaderIn, (int)pname);
        return n;
    }

    public static String glGetShaderInfoLog(int shaderIn, int maxLength) {
        String string;
        if (arbShaders) {
            string = ARBShaderObjects.glGetInfoLogARB((int)shaderIn, (int)maxLength);
            return string;
        }
        string = GL20.glGetShaderInfoLog((int)shaderIn, (int)maxLength);
        return string;
    }

    public static String glGetProgramInfoLog(int program, int maxLength) {
        String string;
        if (arbShaders) {
            string = ARBShaderObjects.glGetInfoLogARB((int)program, (int)maxLength);
            return string;
        }
        string = GL20.glGetProgramInfoLog((int)program, (int)maxLength);
        return string;
    }

    public static void glUseProgram(int program) {
        if (arbShaders) {
            ARBShaderObjects.glUseProgramObjectARB((int)program);
            return;
        }
        GL20.glUseProgram((int)program);
    }

    public static int glCreateProgram() {
        int n;
        if (arbShaders) {
            n = ARBShaderObjects.glCreateProgramObjectARB();
            return n;
        }
        n = GL20.glCreateProgram();
        return n;
    }

    public static void glDeleteProgram(int program) {
        if (arbShaders) {
            ARBShaderObjects.glDeleteObjectARB((int)program);
            return;
        }
        GL20.glDeleteProgram((int)program);
    }

    public static void glLinkProgram(int program) {
        if (arbShaders) {
            ARBShaderObjects.glLinkProgramARB((int)program);
            return;
        }
        GL20.glLinkProgram((int)program);
    }

    public static int glGetUniformLocation(int programObj, CharSequence name) {
        int n;
        if (arbShaders) {
            n = ARBShaderObjects.glGetUniformLocationARB((int)programObj, (CharSequence)name);
            return n;
        }
        n = GL20.glGetUniformLocation((int)programObj, (CharSequence)name);
        return n;
    }

    public static void glUniform1(int location, IntBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform1ARB((int)location, (IntBuffer)values);
            return;
        }
        GL20.glUniform1((int)location, (IntBuffer)values);
    }

    public static void glUniform1i(int location, int v0) {
        if (arbShaders) {
            ARBShaderObjects.glUniform1iARB((int)location, (int)v0);
            return;
        }
        GL20.glUniform1i((int)location, (int)v0);
    }

    public static void glUniform1(int location, FloatBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform1ARB((int)location, (FloatBuffer)values);
            return;
        }
        GL20.glUniform1((int)location, (FloatBuffer)values);
    }

    public static void glUniform2(int location, IntBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform2ARB((int)location, (IntBuffer)values);
            return;
        }
        GL20.glUniform2((int)location, (IntBuffer)values);
    }

    public static void glUniform2(int location, FloatBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform2ARB((int)location, (FloatBuffer)values);
            return;
        }
        GL20.glUniform2((int)location, (FloatBuffer)values);
    }

    public static void glUniform3(int location, IntBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform3ARB((int)location, (IntBuffer)values);
            return;
        }
        GL20.glUniform3((int)location, (IntBuffer)values);
    }

    public static void glUniform3(int location, FloatBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform3ARB((int)location, (FloatBuffer)values);
            return;
        }
        GL20.glUniform3((int)location, (FloatBuffer)values);
    }

    public static void glUniform4(int location, IntBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform4ARB((int)location, (IntBuffer)values);
            return;
        }
        GL20.glUniform4((int)location, (IntBuffer)values);
    }

    public static void glUniform4(int location, FloatBuffer values) {
        if (arbShaders) {
            ARBShaderObjects.glUniform4ARB((int)location, (FloatBuffer)values);
            return;
        }
        GL20.glUniform4((int)location, (FloatBuffer)values);
    }

    public static void glUniformMatrix2(int location, boolean transpose, FloatBuffer matrices) {
        if (arbShaders) {
            ARBShaderObjects.glUniformMatrix2ARB((int)location, (boolean)transpose, (FloatBuffer)matrices);
            return;
        }
        GL20.glUniformMatrix2((int)location, (boolean)transpose, (FloatBuffer)matrices);
    }

    public static void glUniformMatrix3(int location, boolean transpose, FloatBuffer matrices) {
        if (arbShaders) {
            ARBShaderObjects.glUniformMatrix3ARB((int)location, (boolean)transpose, (FloatBuffer)matrices);
            return;
        }
        GL20.glUniformMatrix3((int)location, (boolean)transpose, (FloatBuffer)matrices);
    }

    public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
        if (arbShaders) {
            ARBShaderObjects.glUniformMatrix4ARB((int)location, (boolean)transpose, (FloatBuffer)matrices);
            return;
        }
        GL20.glUniformMatrix4((int)location, (boolean)transpose, (FloatBuffer)matrices);
    }

    public static int glGetAttribLocation(int p_153164_0_, CharSequence p_153164_1_) {
        int n;
        if (arbShaders) {
            n = ARBVertexShader.glGetAttribLocationARB((int)p_153164_0_, (CharSequence)p_153164_1_);
            return n;
        }
        n = GL20.glGetAttribLocation((int)p_153164_0_, (CharSequence)p_153164_1_);
        return n;
    }

    public static int glGenBuffers() {
        int n;
        if (arbVbo) {
            n = ARBVertexBufferObject.glGenBuffersARB();
            return n;
        }
        n = GL15.glGenBuffers();
        return n;
    }

    public static void glBindBuffer(int target, int buffer) {
        if (arbVbo) {
            ARBVertexBufferObject.glBindBufferARB((int)target, (int)buffer);
            return;
        }
        GL15.glBindBuffer((int)target, (int)buffer);
    }

    public static void glBufferData(int target, ByteBuffer data, int usage) {
        if (arbVbo) {
            ARBVertexBufferObject.glBufferDataARB((int)target, (ByteBuffer)data, (int)usage);
            return;
        }
        GL15.glBufferData((int)target, (ByteBuffer)data, (int)usage);
    }

    public static void glDeleteBuffers(int buffer) {
        if (arbVbo) {
            ARBVertexBufferObject.glDeleteBuffersARB((int)buffer);
            return;
        }
        GL15.glDeleteBuffers((int)buffer);
    }

    public static boolean useVbo() {
        if (!vboSupported) return false;
        if (!Minecraft.getMinecraft().gameSettings.useVbo) return false;
        return true;
    }

    public static void glBindFramebuffer(int target, int framebufferIn) {
        if (!framebufferSupported) return;
        switch (framebufferType) {
            case 0: {
                GL30.glBindFramebuffer((int)target, (int)framebufferIn);
                return;
            }
            case 1: {
                ARBFramebufferObject.glBindFramebuffer((int)target, (int)framebufferIn);
                return;
            }
            case 2: {
                EXTFramebufferObject.glBindFramebufferEXT((int)target, (int)framebufferIn);
                return;
            }
        }
    }

    public static void glBindRenderbuffer(int target, int renderbuffer) {
        if (!framebufferSupported) return;
        switch (framebufferType) {
            case 0: {
                GL30.glBindRenderbuffer((int)target, (int)renderbuffer);
                return;
            }
            case 1: {
                ARBFramebufferObject.glBindRenderbuffer((int)target, (int)renderbuffer);
                return;
            }
            case 2: {
                EXTFramebufferObject.glBindRenderbufferEXT((int)target, (int)renderbuffer);
                return;
            }
        }
    }

    public static void glDeleteRenderbuffers(int renderbuffer) {
        if (!framebufferSupported) return;
        switch (framebufferType) {
            case 0: {
                GL30.glDeleteRenderbuffers((int)renderbuffer);
                return;
            }
            case 1: {
                ARBFramebufferObject.glDeleteRenderbuffers((int)renderbuffer);
                return;
            }
            case 2: {
                EXTFramebufferObject.glDeleteRenderbuffersEXT((int)renderbuffer);
                return;
            }
        }
    }

    public static void glDeleteFramebuffers(int framebufferIn) {
        if (!framebufferSupported) return;
        switch (framebufferType) {
            case 0: {
                GL30.glDeleteFramebuffers((int)framebufferIn);
                return;
            }
            case 1: {
                ARBFramebufferObject.glDeleteFramebuffers((int)framebufferIn);
                return;
            }
            case 2: {
                EXTFramebufferObject.glDeleteFramebuffersEXT((int)framebufferIn);
                return;
            }
        }
    }

    public static int glGenFramebuffers() {
        if (!framebufferSupported) {
            return -1;
        }
        switch (framebufferType) {
            case 0: {
                return GL30.glGenFramebuffers();
            }
            case 1: {
                return ARBFramebufferObject.glGenFramebuffers();
            }
            case 2: {
                return EXTFramebufferObject.glGenFramebuffersEXT();
            }
        }
        return -1;
    }

    public static int glGenRenderbuffers() {
        if (!framebufferSupported) {
            return -1;
        }
        switch (framebufferType) {
            case 0: {
                return GL30.glGenRenderbuffers();
            }
            case 1: {
                return ARBFramebufferObject.glGenRenderbuffers();
            }
            case 2: {
                return EXTFramebufferObject.glGenRenderbuffersEXT();
            }
        }
        return -1;
    }

    public static void glRenderbufferStorage(int target, int internalFormat, int width, int height) {
        if (!framebufferSupported) return;
        switch (framebufferType) {
            case 0: {
                GL30.glRenderbufferStorage((int)target, (int)internalFormat, (int)width, (int)height);
                return;
            }
            case 1: {
                ARBFramebufferObject.glRenderbufferStorage((int)target, (int)internalFormat, (int)width, (int)height);
                return;
            }
            case 2: {
                EXTFramebufferObject.glRenderbufferStorageEXT((int)target, (int)internalFormat, (int)width, (int)height);
                return;
            }
        }
    }

    public static void glFramebufferRenderbuffer(int target, int attachment, int renderBufferTarget, int renderBuffer) {
        if (!framebufferSupported) return;
        switch (framebufferType) {
            case 0: {
                GL30.glFramebufferRenderbuffer((int)target, (int)attachment, (int)renderBufferTarget, (int)renderBuffer);
                return;
            }
            case 1: {
                ARBFramebufferObject.glFramebufferRenderbuffer((int)target, (int)attachment, (int)renderBufferTarget, (int)renderBuffer);
                return;
            }
            case 2: {
                EXTFramebufferObject.glFramebufferRenderbufferEXT((int)target, (int)attachment, (int)renderBufferTarget, (int)renderBuffer);
                return;
            }
        }
    }

    public static int glCheckFramebufferStatus(int target) {
        if (!framebufferSupported) {
            return -1;
        }
        switch (framebufferType) {
            case 0: {
                return GL30.glCheckFramebufferStatus((int)target);
            }
            case 1: {
                return ARBFramebufferObject.glCheckFramebufferStatus((int)target);
            }
            case 2: {
                return EXTFramebufferObject.glCheckFramebufferStatusEXT((int)target);
            }
        }
        return -1;
    }

    public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
        if (!framebufferSupported) return;
        switch (framebufferType) {
            case 0: {
                GL30.glFramebufferTexture2D((int)target, (int)attachment, (int)textarget, (int)texture, (int)level);
                return;
            }
            case 1: {
                ARBFramebufferObject.glFramebufferTexture2D((int)target, (int)attachment, (int)textarget, (int)texture, (int)level);
                return;
            }
            case 2: {
                EXTFramebufferObject.glFramebufferTexture2DEXT((int)target, (int)attachment, (int)textarget, (int)texture, (int)level);
                return;
            }
        }
    }

    public static void setActiveTexture(int texture) {
        if (arbMultitexture) {
            ARBMultitexture.glActiveTextureARB((int)texture);
            return;
        }
        GL13.glActiveTexture((int)texture);
    }

    public static void setClientActiveTexture(int texture) {
        if (arbMultitexture) {
            ARBMultitexture.glClientActiveTextureARB((int)texture);
            return;
        }
        GL13.glClientActiveTexture((int)texture);
    }

    public static void setLightmapTextureCoords(int target, float p_77475_1_, float p_77475_2_) {
        if (arbMultitexture) {
            ARBMultitexture.glMultiTexCoord2fARB((int)target, (float)p_77475_1_, (float)p_77475_2_);
            return;
        }
        GL13.glMultiTexCoord2f((int)target, (float)p_77475_1_, (float)p_77475_2_);
    }

    public static void glBlendFunc(int sFactorRGB, int dFactorRGB, int sfactorAlpha, int dfactorAlpha) {
        if (!openGL14) {
            GL11.glBlendFunc((int)sFactorRGB, (int)dFactorRGB);
            return;
        }
        if (extBlendFuncSeparate) {
            EXTBlendFuncSeparate.glBlendFuncSeparateEXT((int)sFactorRGB, (int)dFactorRGB, (int)sfactorAlpha, (int)dfactorAlpha);
            return;
        }
        GL14.glBlendFuncSeparate((int)sFactorRGB, (int)dFactorRGB, (int)sfactorAlpha, (int)dfactorAlpha);
    }

    public static boolean isFramebufferEnabled() {
        if (!framebufferSupported) return false;
        if (!Minecraft.getMinecraft().gameSettings.fboEnable) return false;
        return true;
    }

    public static String func_183029_j() {
        if (field_183030_aa == null) {
            return "<unknown>";
        }
        String string = field_183030_aa;
        return string;
    }

    static {
        logText = "";
    }
}

