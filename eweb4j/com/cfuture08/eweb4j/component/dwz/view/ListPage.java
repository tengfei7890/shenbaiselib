package com.cfuture08.eweb4j.component.dwz.view;

import java.util.List;

import com.cfuture08.eweb4j.component.DivPageComp;

/**
 * 列表页视图数据对象
 * 
 * @author weiwei
 * 
 */
public class ListPage {
	private String model;
	private SearchForm searchForm;
	private List<?> pojos;
	private DivPageComp dpc;

	public ListPage(String model, SearchForm searchForm, List<?> pojos,
			DivPageComp dpc) {
		this.model = model;
		this.searchForm = searchForm;
		this.pojos = pojos;
		this.dpc = dpc;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public SearchForm getSearchForm() {
		return searchForm;
	}

	public void setSearchForm(SearchForm searchForm) {
		this.searchForm = searchForm;
	}

	public List<?> getPojos() {
		return pojos;
	}

	public void setPojos(List<?> pojos) {
		this.pojos = pojos;
	}

	public DivPageComp getDpc() {
		return dpc;
	}

	public void setDpc(DivPageComp dpc) {
		this.dpc = dpc;
	}

}
