
public class Demand1 implements IDemand {

	@Override
	public Character[] operatorGeneration() {
		// ȷ���������Ͳ��������ܸ���
		int n=3;
		Character[] infixExpression = new Character[n];
		// ���������
		int index = (((int) (Math.random() * 10)) % 2);
		infixExpression[n-1] = Operator[index];
		return infixExpression;
	}

	@Override
	public void operandGeneration() {
		// TODO �Զ����ɵķ������

	}

}
