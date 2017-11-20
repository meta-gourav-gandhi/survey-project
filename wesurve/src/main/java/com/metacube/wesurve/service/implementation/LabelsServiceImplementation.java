/**
 * The LabelsServiceImplementation class is service class for Labels model.
 */
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

	/**
	 * This method returns Labels object with label name as the given label
	 * @param label - String
	 * @return Labels object
	 */
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