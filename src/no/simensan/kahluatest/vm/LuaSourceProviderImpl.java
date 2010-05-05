package no.simensan.kahluatest.vm;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import se.krka.kahlua.require.LuaSourceProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class LuaSourceProviderImpl implements LuaSourceProvider {

    public Reader getLuaSource(String key) {
        try {
			URL url = Resources.getResource(key);
			return CharStreams.newReaderSupplier(Resources.newInputStreamSupplier(url), Charsets.UTF_8).getInput();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
}
