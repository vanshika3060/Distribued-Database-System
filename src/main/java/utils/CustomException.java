package utils;
public class CustomException extends Exception {

        private String exceptionMessage;

        public CustomException(String exceptionMessage) {
            super(exceptionMessage);
            this.exceptionMessage = exceptionMessage;
        }

        public String getErrorMessage() {
            return exceptionMessage;
        }

        @Override
        public String toString() {
            return "Exception{" +
                    "errorMessage='" + exceptionMessage + '\'' +
                    '}';
        }
    }
