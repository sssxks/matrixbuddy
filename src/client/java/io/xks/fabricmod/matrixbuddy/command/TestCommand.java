package io.xks.fabricmod.matrixbuddy.command;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestCommand extends Command {
    protected TestCommand(IBaritone baritone, String... names) {
        super(baritone, names);
    }

    @Override
    public void execute(String s, IArgConsumer iArgConsumer) throws CommandException {

    }

    @Override
    public Stream<String> tabComplete(String s, IArgConsumer iArgConsumer) throws CommandException {
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "test Matrix buddy";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The come command test.", "", "dsade.", "", "Usage:", "> test");
    }
}
