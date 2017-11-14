package com.metacube.wesurve.dao.labels;

import com.metacube.wesurve.dao.AbstractDao;
import com.metacube.wesurve.model.Labels;

public interface LabelsDao extends AbstractDao<Labels, Integer> {
	Labels getByLabelName(String label);
	Iterable<Labels> getLabelsByNameAlike(String searchString);
}