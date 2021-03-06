// Copyright (c) 2003-2012, Jodd Team (jodd.org). All Rights Reserved.

package jodd.lagarto.dom;

import jodd.lagarto.TagWriterUtil;

import java.io.IOException;

/**
 * CDATA node.
 */
public class CData extends Node {

	public CData(String cdata) {
		super(NodeType.CDATA, null, true);
		this.nodeValue = cdata;
	}

	@Override
	public CData clone() {
		return cloneTo(new CData(nodeValue));
	}

	@Override
	public void toHtml(Appendable appendable) throws IOException {
		TagWriterUtil.writeCData(appendable, nodeValue);
	}
}