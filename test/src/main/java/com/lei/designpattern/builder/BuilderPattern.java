package com.lei.designpattern.builder;

public class BuilderPattern {

    private static class Computer{
        private String cup;
        private String memory;
        private String disc;
        private String gpu;

        @Override
        public String toString() {
            return "Computer{" +
                    "cup='" + cup + '\'' +
                    ", memory='" + memory + '\'' +
                    ", disc='" + disc + '\'' +
                    ", gpu='" + gpu + '\'' +
                    '}';
        }

        public String getCup() {
            return cup;
        }

        public void setCup(String cup) {
            this.cup = cup;
        }

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public String getDisc() {
            return disc;
        }

        public void setDisc(String disc) {
            this.disc = disc;
        }

        public String getGpu() {
            return gpu;
        }

        public void setGpu(String gpu) {
            this.gpu = gpu;
        }
    }

    private static class ComputerBuilder{
        private Computer computer;

        public ComputerBuilder() {
        }

        public ComputerBuilder cpu(String cpu){
            computer.setCup(cpu);
            return this;
        }
        public ComputerBuilder gpu(String gpu){
            computer.setGpu(gpu);
            return this;
        }
        public ComputerBuilder memory(String memory){
            computer.setMemory(memory);
            return this;
        }
        public ComputerBuilder disc(String disc){
            computer.setDisc(disc);
            return this;
        }

        public ComputerBuilder create(){
            computer = new Computer();
            return this;
        }

        public Computer build(){
            return computer;
        }

    }

    public static void main(String[] args) {
        ComputerBuilder builder = new ComputerBuilder();
        Computer computer = builder.create().cpu("i3").disc("500g").memory("8G").gpu("820").build();
        System.out.println(computer.toString());
    }
}
