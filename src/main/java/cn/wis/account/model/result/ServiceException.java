package cn.wis.account.model.result;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 3050494057368647888L;

	private ResultEnum resultEnum;

	public ServiceException(ResultEnum resultEnum) {
		super("");
        this.resultEnum = resultEnum;
    }

    public ServiceException(ResultEnum resultEnum, String message) {
    	super(message);
        this.resultEnum = resultEnum;
    }

    public ResultEnum getResultEnum() {
        return resultEnum;
    }

}
