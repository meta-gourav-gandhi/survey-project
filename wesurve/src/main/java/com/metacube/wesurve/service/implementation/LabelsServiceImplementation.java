package com.metacube.wesurve.service.implementation;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.labels.LabelsDao;
import com.metacube.wesurve.model.Labels;
import com.metacube.wesurve.service.LabelsService;

@Service("labelService")
public class LabelsServiceImplementation implements LabelsService {

	@Resource(name = "hibernateLabelsDaoImplementation")
	LabelsDao labelsDao;

	@Override
	public Labels getLabelByLabelName(String label) {
		Labels labelModel;
		try {
			labelModel = labelsDao.getByLabelName(label);
		} catch (Exception exception) {
			labelModel = null;
			exception.printStackTrace();
		}

		return labelModel;
	}
}