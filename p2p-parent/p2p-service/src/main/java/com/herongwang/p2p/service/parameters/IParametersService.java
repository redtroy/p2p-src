package com.herongwang.p2p.service.parameters;

import java.util.List;

import com.herongwang.p2p.entity.parameters.ParametersEntity;
import com.sxj.util.exception.ServiceException;

public interface IParametersService {
	/**
	 * 新增参数
	 * @param apply
	 */
	public void addParameters(ParametersEntity p) throws ServiceException;

	/**
	 * 更新参数
	 * 
	 * @param apply
	 */
	public void updateParameters(ParametersEntity p) throws ServiceException;

	/**
	 * 获取参数信息
	 * 
	 * @param id
	 * @return
	 */
	public ParametersEntity getParametersEntity(String id) throws ServiceException;

	/**
	 * 获取参数列表
	 * 
	 * @param query
	 * @return
	 */
	public List<ParametersEntity> queryParameters(ParametersEntity query)
			throws ServiceException;

	/**
	 * 删除参数
	 * 
	 * @param id
	 */
	public void delParameters(String id) throws ServiceException;
}
