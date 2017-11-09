package com.metacube.wesurve.service;

import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.metacube.wesurve.dao.labels.LabelsDao;
import com.metacube.wesurve.model.Labels;

@Service("labelService")
@Transactional
public class LabelsServiceImplementation implements LabelsService {

	@Resource(name = "hibernateLabelsDaoImplementation")
	LabelsDao labelsDao;
	
	@Override
	public Set<Labels> getAll() {
		return (Set<Labels>) labelsDao.findAll();
	}

	@Override
	public Labels getLabelByLabelName(String label) {
		return labelsDao.getByLabelName(label);
	}
}