final public value complex extends x10.lang.Object {
	final int re;
	final int im;
	complex(int re, int im) {
		this.re = re;
		this.im = im;
	}
	complex add (complex other) {
		return new complex(this.re+other.re, this.im+other.im);
	}
}

