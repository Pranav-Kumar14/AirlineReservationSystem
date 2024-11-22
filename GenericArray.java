package airline;
import java.util.Iterator;

class GenericArray<T> implements Iterable<T>{
    public T[] array;
    public int size;

    @SuppressWarnings("unchecked")
    public GenericArray() {
        array = (T[]) new Object[200]; 
        size = 0;
    }

    public void add(T element) {
        array[size++] = element; 
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return array[index]; 
    }
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            public int currentIndex = 0;

            public boolean hasNext() {
                return currentIndex < size;
            }

            public T next() {
                return array[currentIndex++];
            }
        };
    }
}