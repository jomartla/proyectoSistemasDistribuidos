package Proyecto;

import java.util.concurrent.CyclicBarrier;

public class AtenderPeticionCliente implements Runnable {
	
	CyclicBarrier cb;
	
	public AtenderPeticionCliente(CyclicBarrier barrera){
		cb = barrera;
	}

	@Override
	public void run() {
				
	}
}
