package CommonTools;

public class LoadingThread extends Thread {
	private boolean mKeepRunning = true;
	private int mProgressCounter = 0;
	private int mExecDelay;
	
	public LoadingThread(int execDelay){
		mExecDelay = execDelay;
	}
	
	public void stopRunning(){
		mKeepRunning = false;
		System.out.println("");
	}
	
	public void run(){
		while(mKeepRunning == true){
			this.printLoadingString();
			try {
				Thread.sleep(mExecDelay);
			} catch (InterruptedException e) {
				CommonTools.processError("Thread Interruption Error");
			}
		}
	}
	
	private void printLoadingString(){
		System.out.print(getLoadingCharacter(mProgressCounter));
		mProgressCounter = (mProgressCounter + 1) % 4;
	}
	
	private String getLoadingCharacter(int loadingNumber){
		switch(loadingNumber){
			case 0:
				return "|";
			case 1:
				return "/";
			case 2:
				return "-";
			case 3:
				return "\\";
		}
		return "0";
	}

}
