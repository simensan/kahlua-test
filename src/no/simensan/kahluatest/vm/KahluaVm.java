package no.simensan.kahluatest.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.krka.kahlua.converter.KahluaEnumConverter;
import se.krka.kahlua.converter.KahluaTableConverter;
import se.krka.kahlua.converter.LuaConverterManager;
import se.krka.kahlua.converter.LuaNumberConverter;
import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.integration.LuaReturn;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.require.LuaSourceProvider;
import se.krka.kahlua.require.Require;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class KahluaVm {
    private static final Logger logger = LoggerFactory.getLogger(KahluaVm.class);

    protected LuaConverterManager luaConverterManager;
	protected LuaCaller luaCaller;
	protected KahluaThread kahluaThread;
	protected Platform javaPlatform;
	protected KahluaTable kahluaEnvironment;
	protected LuaJavaClassExposer luaJavaClassExposer;
    protected LuaSourceProvider luaSourceProvider;

    public KahluaVm() {
        luaSourceProvider = new LuaSourceProviderImpl();
        javaPlatform = new J2SEPlatform();
		kahluaEnvironment = javaPlatform.newEnvironment();
		LuaCompiler.register(kahluaEnvironment);

		luaConverterManager = new LuaConverterManager();
		LuaNumberConverter.install(luaConverterManager);
		new KahluaTableConverter(javaPlatform).install(luaConverterManager);
		KahluaEnumConverter.install(luaConverterManager);

        luaCaller = new LuaCaller(luaConverterManager);
		luaJavaClassExposer = new NotPooLuaJavaClassExposer(luaConverterManager, javaPlatform, kahluaEnvironment);
		kahluaThread = new KahluaThread(javaPlatform, kahluaEnvironment);
        new Require(luaSourceProvider).install(kahluaEnvironment);
    }

    public LuaReturn loadLuaFromFile(String luaFile) {
        LuaClosure luaClosure;
        try {
			Reader source = luaSourceProvider.getLuaSource(luaFile);
			if (source == null) {
				throw new RuntimeException("Could not find lua source file: " + luaFile);
			}
			luaClosure = LuaCompiler.loadis(source, luaFile, kahluaEnvironment);
		} catch (IOException e) {
            e.printStackTrace();
			throw new RuntimeException(e);
		}

        return luaCall(luaClosure);
    }

    public LuaReturn luaCall(Object functionObject, Object... args) {
        return luaCaller.protectedCall(kahluaThread, functionObject, args);
    }

    public KahluaTable getEnvironment() {
        return kahluaEnvironment;
    }

    public LuaJavaClassExposer getLuaJavaClassExposer() {
        return luaJavaClassExposer;
    }
}
