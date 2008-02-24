package colab.common.channel;

enum ChannelType {
  CHAT{public String toString(){return "Chat";}},
  WHITE_BOARD{public String toString(){return "White Board";}},
  DOCUMENT{public String toString(){return "Document";}}
};

public class ChannelDescriptor {

  private String name;
  private ChannelType type;
  
  public ChannelDescriptor(String name, ChannelType type)
  {
    this.name = new String(name);
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public ChannelType getType()
  {
    return type;
  }

  public void setType(ChannelType type)
  {
    this.type = type;
  }
}
