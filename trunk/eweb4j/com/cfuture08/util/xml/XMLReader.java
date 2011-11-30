package com.cfuture08.util.xml;

import java.util.List;


public interface XMLReader {
	<T> List<T> read() throws Exception;
	<T> T readOne() throws Exception;
}
